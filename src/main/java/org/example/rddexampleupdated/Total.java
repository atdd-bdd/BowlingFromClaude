package org.example.rddexampleupdated;

import java.util.Objects;

/**
 * Represents the overall total of holdings
 */
public class Total {
    private final Money overallTotal;

    public Total(Money overallTotal) {
        this.overallTotal = overallTotal != null ? overallTotal : new Money(0, Currency.USD);
    }

    public Total() {
        this(new Money(0, Currency.USD));
    }

    public Money getOverallTotal() {
        return overallTotal;
    }

    @Override
    public String toString() {
        return String.format("Total{overallTotal=%s}", overallTotal);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Total total = (Total) obj;
        return Objects.equals(overallTotal, total.overallTotal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(overallTotal);
    }
}