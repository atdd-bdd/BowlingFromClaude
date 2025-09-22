package org.example.rddexampleupdated;
import java.util.Objects;

/**
 * Represents a financial instrument (stock, bond, etc.)
 */
public class Instrument {
    private final String symbol;

    public Instrument(String symbol) {
        if (symbol == null || symbol.trim().isEmpty()) {
            throw new IllegalArgumentException("Instrument symbol cannot be null or empty");
        }
        this.symbol = symbol.trim().toUpperCase();
    }

    public String getSymbol() {
        return symbol;
    }

    public static Instrument of(String symbol) {
        return new Instrument(symbol);
    }

    @Override
    public String toString() {
        return symbol;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Instrument that = (Instrument) obj;
        return Objects.equals(symbol, that.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol);
    }
}