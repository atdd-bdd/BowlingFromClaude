package org.example.dollar;

// DollarValidationResult.java
public class DollarValidationResult {
    private final boolean valid;
    private final String message;
    private final String notes;

    public DollarValidationResult(boolean valid, String message, String notes) {
        this.valid = valid;
        this.message = message;
        this.notes = notes;
    }

    public DollarValidationResult(boolean valid, String message) {
        this(valid, message, "");
    }

    public boolean isValid() { return valid; }
    public String getMessage() { return message; }
    public String getNotes() { return notes; }

    @Override
    public String toString() {
        return String.format("Valid: %s, Message: %s, Notes: %s", valid, message, notes);
    }
}
