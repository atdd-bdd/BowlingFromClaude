package org.example.filter;

public class ID {
    private String value;
    private boolean valid;

    public ID() {
        this.value = "Q0000";
        this.valid = isValidID(this.value);
    }

    public ID(String value) {
        this.value = value != null ? value : "Q0000";
        this.valid = isValidID(this.value);
    }

    private boolean isValidID(String id) {
        if (id == null || id.length() != 5) {
            return false;
        }

        if (!id.startsWith("Q")) {
            return false;
        }

        return true;
    }

    public String getValue() { return value; }
    public void setValue(String value) {
        this.value = value != null ? value : "Q0000";
        this.valid = isValidID(this.value);
    }
    public boolean isValid() { return valid; }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ID id = (ID) obj;
        return value.equals(id.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
