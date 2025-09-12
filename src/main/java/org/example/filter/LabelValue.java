package org.example.filter;

public class LabelValue {
    private ID id;
    private int value;

    public LabelValue() {
        this.id = new ID();
        this.value = 0;
    }

    public LabelValue(String id, int value) {
        this.id = new ID(id);
        this.value = value;
    }

    public LabelValue(ID id, int value) {
        this.id = id;
        this.value = value;
    }

    // Getters and setters
    public ID getId() { return id; }
    public void setId(ID id) { this.id = id; }
    public void setId(String id) { this.id = new ID(id); }
    public int getValue() { return value; }
    public void setValue(int value) { this.value = value; }

    @Override
    public String toString() {
        return String.format("LabelValue(id=%s, value=%d)", id, value);
    }
}