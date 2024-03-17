package com.dlsc.preferencesfx.formsfx.view.controls;

import com.dlsc.preferencesfx.util.VisibilityProperty;
import java.time.LocalDate;
import java.util.Objects;
import javafx.geometry.Pos;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class SimpleDateControl extends SimpleControl<SimpleDateField, StackPane> {

    protected Label fieldLabel;
    protected DatePicker picker;
    protected Label readOnlyLabel;
    protected StackPane stack;
    private final LocalDate initialValue;

    public SimpleDateControl(LocalDate initialValue) {
      Objects.requireNonNull(initialValue);
      this.initialValue = initialValue;
    }

    public static SimpleDateControl of(LocalDate initialValue, VisibilityProperty visibilityProperty) {
      SimpleDateControl simpleColorPickerControl = new SimpleDateControl(initialValue);
      simpleColorPickerControl.visibilityProperty = visibilityProperty;
      return simpleColorPickerControl;
    }

    @Override
    public void initializeParts() {
        super.initializeParts();

        stack = new StackPane();
        fieldLabel = new Label();
        readOnlyLabel = new Label();
        picker = new DatePicker(initialValue);
        picker.setEditable(true);

      node = new StackPane();
      node.getStyleClass().add("simple-text-control");

      picker.setMaxWidth(Double.MAX_VALUE);
      picker.valueProperty().addListener((o,old,value) -> {
          if (!field.valueProperty().getValue().equals(value)) {
              field.valueProperty().setValue(value);
            }
      });
      
      field.valueProperty().setValue(picker.getValue());
      fieldLabel = new Label(field.labelProperty().getValue());
    }

    @Override
    public void layoutParts() {
      node.getChildren().addAll(picker);
      node.setAlignment(Pos.CENTER_LEFT);
    }

    @Override
    public void setupBindings() {
      super.setupBindings();
    }

    @Override
    public void setupValueChangedListeners() {
      super.setupValueChangedListeners();

      field.valueProperty().addListener((observable, oldValue, newValue) -> {
        if (null != newValue) {
          if (!Objects.equals(newValue, picker.getValue())) {
            picker.setValue(newValue);
          }
        }
      });

      field.errorMessagesProperty().addListener(
          (observable, oldValue, newValue) -> toggleTooltip(picker)
      );
      field.tooltipProperty().addListener(
          (observable, oldValue, newValue) -> toggleTooltip(picker)
      );
      picker.focusedProperty().addListener(
          (observable, oldValue, newValue) -> toggleTooltip(picker)
      );
    }
}
