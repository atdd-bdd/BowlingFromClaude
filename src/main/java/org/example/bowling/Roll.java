package org.example.bowling;

public class Roll {
    private String value;
    private boolean valid;
    private String notes;

    public static final String TBR = "TBR"; // To Be Rolled

    public Roll() {
        this.value = "0";
        this.valid = false;
        this.notes = "";
    }

    public Roll(String value) {
        this.value = value;
        this.valid = isValidRoll(value);
        this.notes = "";
    }

    public Roll(int value) {
        this.value = String.valueOf(value);
        this.valid = isValidRoll(this.value);
        this.notes = "";
    }

    private boolean isValidRoll(String value) {
        if (TBR.equals(value)) {
            return true;
        }
        try {
            int intValue = Integer.parseInt(value);
            return intValue >= 0 && intValue <= 10;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public int getIntValue() {
        if (TBR.equals(value)) {
            return -1;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // Getters and setters
    public String getValue() { return value; }
    public void setValue(String value) {
        this.value = value;
        this.valid = isValidRoll(value);
    }
    public boolean isValid() { return valid; }
    public void setValid(boolean valid) { this.valid = valid; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public String toString() {
        return value;
    }
}