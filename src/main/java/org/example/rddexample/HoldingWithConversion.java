package org.example.rddexample;

import java.util.Objects;

/**
 * Represents a holding with its converted total in a target currency
 */
public class HoldingWithConversion {
    private final Holding holding;
    private final Money totalInTargetCurrency;

    public HoldingWithConversion(Holding holding, Money totalInTargetCurrency) {
        this.holding = holding;
        this.totalInTargetCurrency = totalInTargetCurrency;
    }

    public Holding getHolding() {
        return holding;
    }

    public String getInstrument() {
        return holding.getInstrument();
    }

    public Money getShares() {
        return new Money(holding.getShares(), Currency.USD); // Shares as money for display
    }

    public Money getPrice() {
        return holding.getPrice();
    }

    public Money getTotal() {
        return holding.getTotal();
    }

    public Money getTotalInTargetCurrency() {
        return totalInTargetCurrency;
    }

    @Override
    public String toString() {
        return String.format("HoldingWithConversion{instrument='%s', shares=%s, price=%s, total=%s, totalInTargetCurrency=%s}",
                getInstrument(), holding.getShares(), getPrice(), getTotal(), getTotalInTargetCurrency());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        HoldingWithConversion that = (HoldingWithConversion) obj;
        return Objects.equals(holding, that.holding) &&
                Objects.equals(totalInTargetCurrency, that.totalInTargetCurrency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(holding, totalInTargetCurrency);
    }
}