package com.bicycle.quant.script.editor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.fxmisc.richtext.util.UndoUtils;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.control.IndexRange;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Border;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class ScriptEditor extends CodeArea {
    
    private static final String[] ACTION_NAMES = new String[] {
            "buy", "sell", "short", "cover", "optimize"
    };
    
    private static final String[] RULE_NAMES = new String[] {
            "dayOfWeek", "weekOfMonth", "month", "stopGain", "stopLoss", "after", "bars"
    };
    
    private static final String[] INDICATOR_NAMES = new String[] {
            "open", "high", "low",
            "close", "volume", "trueRange", "typicalPrice", "closeLocation",
            "const", "chop", "cci", "atr", "loss", "gain", "sma", "ema", "mma",
            "rsi", "fallingStrength", "risingStrength", "highest", "lowest",
            "previous", "variance", "meanDev", "stdDev"
    };

    private static final Pattern WHITESPACE = Pattern.compile( "^\\s+" );
    private static final String ACTION_NAME_PATTERN = "\\b(" + String.join("|", ACTION_NAMES) + ")\\b";
    private static final String RULE_NAME_PATTERN = "\\b(" + String.join("|", RULE_NAMES) + ")\\b";
    private static final String INDICATOR_NAME_PATTERN = "\\b(" + String.join("|", INDICATOR_NAMES) + ")\\b";
    private static final String PAREN_PATTERN = "[()]";
    private static final String BRACE_PATTERN = "[{}]";
    private static final String BRACKET_PATTERN = "[\\[\\]]";
    private static final String SEMICOLON_PATTERN = ";";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/"   // for whole text processing (text blocks)
            + "|" + "/\\*[^\\v]*" + "|" + "^\\h*\\*([^\\v]*|/)";  // for visible paragraph processing (line by line)

    private static final Pattern PATTERN = Pattern.compile(
            "(?<INDICATOR>" + INDICATOR_NAME_PATTERN + ")"
                    + "|(?<ACTION>" + ACTION_NAME_PATTERN + ")"
                    + "|(?<RULE>" + RULE_NAME_PATTERN + ")"
                    + "|(?<PAREN>" + PAREN_PATTERN + ")"
                    + "|(?<BRACE>" + BRACE_PATTERN + ")"
                    + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                    + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                    + "|(?<STRING>" + STRING_PATTERN + ")"
                    + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
    );
    
    
    private final Stage stage;
    private Popup autoCompletionPopup;
    private ListView<String> autoCompletionList;
    private final Suggestion completion;

    public ScriptEditor(Stage stage) {
        this.stage = stage;
        autoCompletionPopup = new Popup();
        autoCompletionPopup.setAutoHide(true);
        autoCompletionPopup.setHideOnEscape(true);
        final List<String> words = new ArrayList<>();
        words.addAll(Arrays.asList(ACTION_NAMES));
        words.addAll(Arrays.asList(RULE_NAMES));
        words.addAll(Arrays.asList(INDICATOR_NAMES));
        completion = new Suggestion(words);
        setUndoManager(UndoUtils.plainTextUndoManager(this));
        setParagraphGraphicFactory(LineNumberFactory.get(this));
        plainTextChanges()
            .filter(ch -> !ch.getInserted().equals(ch.getRemoved()))
            .subscribe(change -> setStyleSpans(0, computeHighlighting(getText())));
        autoIndent();
        autoCompleteBrackets();
        configureAllShortcuts();
        richChanges().filter(ch -> !ch.getInserted().equals(ch.getRemoved())).subscribe(x -> {
            autoCompletion(getCurrWord());
        });
        requestFocus();
    }
    
    private void configureAllShortcuts() {
        final KeyCombination copyComb = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN);
        final KeyCombination showCompletionComb = new KeyCodeCombination(KeyCode.SPACE, KeyCombination.CONTROL_DOWN);
        final KeyCombination cutComb = new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN);
        final KeyCombination ctrlEnterComb = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.CONTROL_DOWN);
        addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (copyComb.match(event)) {
                copy();
                event.consume();
            } else if (showCompletionComb.match(event)) {
                autoCompletion(getCurrWord());
            } else if (cutComb.match(event)) {
                cut();
                event.consume();
            } else if (ctrlEnterComb.match(event)) {
                selectLine();
                IndexRange range = getCaretSelectionBind().getRange();
                insertText(range.getEnd(), "\n");
                indent();
            }
        });
    }

    private String getCurrWord() {
        Set<Character> charSet = new HashSet<>();
        charSet.add('}');
        charSet.add('{');
        charSet.add(']');
        charSet.add('[');
        charSet.add(')');
        charSet.add('(');
        StringBuilder word = new StringBuilder();
        for (int i = getCaretPosition(); i>0; i--) {
            char ch = getText().charAt(i-1);
            if (ch == ' ' || ch == '\n' || charSet.contains(ch)) break;
            else word.append(ch);
        }
        return word.reverse().toString();
    }
    
    private void autoIndent() {
        addEventHandler( KeyEvent.KEY_PRESSED, KE -> {
            if ( KE.getCode() == KeyCode.ENTER && !KE.isControlDown()) indent();
        });
    }
    
    private void indent() {
        int caretPosition = getCaretPosition();
        int currentParagraph = getCurrentParagraph();
        Matcher m0 = WHITESPACE.matcher( getParagraph( currentParagraph-1 ).getSegments().get( 0 ) );
        if ( m0.find() ) insertText( caretPosition, m0.group());
    }

    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        while(matcher.find()) {
            String styleClass =
                    matcher.group("INDICATOR") != null ? "indicator" :
                        matcher.group("ACTION") != null ? "action" :
                            matcher.group("RULE") != null ? "rule" :
                                matcher.group("PAREN") != null ? "paren" :
                                    matcher.group("BRACE") != null ? "brace" :
                                            matcher.group("BRACKET") != null ? "bracket" :
                                                    matcher.group("SEMICOLON") != null ? "semicolon" :
                                                            matcher.group("STRING") != null ? "string" :
                                                                    matcher.group("COMMENT") != null ? "comment" :
                                                                            null; /* never happens */ assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }
    
    private void autoCompleteBrackets() {
        setOnKeyTyped(keyEvent -> {
            String str = keyEvent.getCharacter().equals("{") ? "}" :
                    keyEvent.getCharacter().equals("[") ? "]" :
                            keyEvent.getCharacter().equals("(") ? ")" :
                                    keyEvent.getCharacter().equals("\"") ? "\"" :
                                            keyEvent.getCharacter().equals("'") ? "'" : null;
            if (str==null) return;
            insertText(getCaretPosition(), str);
            getCaretSelectionBind().moveTo(getCaretPosition()-1);
        });
    }

    private void autoCompletion(String toSearch) {
        if (toSearch.length()==0) {
            autoCompletionPopup.hide();
            return;
        }
        showCompletion(completion.suggest(toSearch), toSearch.length());
    }

    public void insertCompletion(String s, int wordLen) {
        replaceText(getCaretPosition()-wordLen, getCaretPosition(), s);
        autoCompletionPopup.hide();
    }

    private void showCompletion(List<String> list, int wordLen) {
        if (list.size()>0) {
            autoCompletionList = new ListView<>();
            autoCompletionList.setPrefWidth(140);
            autoCompletionList.setBorder(Border.EMPTY);
            autoCompletionList.setCellFactory(listView -> new ListCell<>() {
                { setMaxHeight(10); setPadding(new Insets(1));}
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if(null == item || empty) {
                        setText(null);
                    } else {
                        setText(item);
                    }
                }
            });
            autoCompletionList.getItems().addAll(list);
            autoCompletionList.prefHeightProperty().bind(Bindings.size(autoCompletionList.getItems()).multiply(24));
            autoCompletionList.getSelectionModel().selectFirst();

            // set listeners
            autoCompletionList.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode().equals(KeyCode.ENTER)) insertCompletion(autoCompletionList.getSelectionModel().getSelectedItem(), wordLen);
            });
            autoCompletionList.setOnMouseClicked(event -> {
                if (autoCompletionList.getSelectionModel().getSelectedItem()!=null) insertCompletion(autoCompletionList.getSelectionModel().getSelectedItem(), wordLen);
            });

            // replacing old data with new one.
            autoCompletionPopup.getContent().clear();
            autoCompletionPopup.getContent().add(autoCompletionList);
            autoCompletionPopup.show(stage, getCaretBounds().get().getMaxX(), getCaretBounds().get().getMaxY());
        } else {
            if (autoCompletionPopup !=null) {
                autoCompletionPopup.getContent().clear();
                autoCompletionPopup.hide();
            }
        }
    }

    
}
