package org.example.fractions;


/**
 * Enum representing supported mathematical operators
 */
public enum Operator {
    ADD("+", "Addition"),
    MULTIPLY("*", "Multiplication");

    private final String symbol;
    private final String description;

    Operator(String symbol, String description) {
        this.symbol = symbol;
        this.description = description;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getDescription() {
        return description;
    }

    public Fraction apply(Fraction left, Fraction right) {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Cannot apply operator to null fractions");
        }

        switch (this) {
            case ADD:
                return left.add(right);
            case MULTIPLY:
                return left.multiply(right);
            default:
                throw new UnsupportedOperationException("Operator not implemented: " + this);
        }
    }

    public static Operator fromSymbol(String symbol) {
        if (symbol == null || symbol.trim().isEmpty()) {
            throw new IllegalArgumentException("Operator symbol cannot be null or empty");
        }

        String trimmedSymbol = symbol.trim();
        for (Operator op : values()) {
            if (op.symbol.equals(trimmedSymbol)) {
                return op;
            }
        }

        throw new IllegalArgumentException("Unknown operator symbol: " + symbol);
    }

    @Override
    public String toString() {
        return symbol;
    }
}