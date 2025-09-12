package org.example.filter;

public class ValueValid {
    private String value;
    private boolean valid;
    private String notes;

    public ValueValid() {
        this.value = "0";
        this.valid = false;
        this.notes = "";
    }

    public ValueValid(String value, boolean valid, String notes) {
        this.value = value;
        this.valid = valid;
        this.notes = notes;
    }

    // Getters and setters
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    public boolean isValid() { return valid; }
    public void setValid(boolean valid) { this.valid = valid; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public String toString() {
        return String.format("ValueValid(value='%s', valid=%b, notes='%s')", value, valid, notes);
    }
}