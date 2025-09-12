package org.example.filter;

public class FilterValue {
    private String name;
    private ID value;

    public FilterValue() {
        this.name = "";
        this.value = new ID("Q0000");
    }

    public FilterValue(String name, String value) {
        this.name = name;
        this.value = new ID(value);
    }

    public FilterValue(String name, ID value) {
        this.name = name;
        this.value = value;
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public ID getValue() { return value; }
    public void setValue(ID value) { this.value = value; }
    public void setValue(String value) { this.value = new ID(value); }

    @Override
    public String toString() {
        return String.format("FilterValue(name='%s', value=%s)", name, value);
    }
}
