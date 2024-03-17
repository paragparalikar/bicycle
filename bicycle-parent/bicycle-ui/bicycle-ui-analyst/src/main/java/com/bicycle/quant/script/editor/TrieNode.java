package com.bicycle.quant.script.editor;

import java.util.HashMap;
import java.util.Map;

class TrieNode {
    
    char c;
    boolean isWord;
    final Map<Character, TrieNode> children = new HashMap<>();
    
    public TrieNode() {
        
    }
    
    public TrieNode(char c) {
        this.c = c;
    }

    public void insert(String word) {
        if (word == null || word.isEmpty())
            return;
        char firstChar = word.charAt(0);
        TrieNode child = children.get(firstChar);
        if (child == null) {
            child = new TrieNode(firstChar);
            children.put(firstChar, child);
        }
        if (word.length() > 1)
            child.insert(word.substring(1));
        else
            child.isWord = true;
    }

}