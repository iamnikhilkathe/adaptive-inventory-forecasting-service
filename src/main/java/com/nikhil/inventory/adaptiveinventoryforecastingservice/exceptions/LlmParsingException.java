package com.nikhil.inventory.adaptiveinventoryforecastingservice.exceptions;

import java.util.List;

public class LlmParsingException extends RuntimeException {
    private final List<String> ambiguousFields;
    public LlmParsingException(String message, List<String> ambiguousFields) {
        super(message);
        this.ambiguousFields = ambiguousFields;
    }
    public List<String> getAmbiguousFields() { return ambiguousFields; }
}
