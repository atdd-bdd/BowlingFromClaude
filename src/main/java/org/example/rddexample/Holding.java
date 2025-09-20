package org.example.rddexample;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents a financial holding with instrument, shares, price, and total
 */
public class Holding {
    private final String instrument;
    private final BigDecimal shares;
    private final Money price;
    private final Money total;

    public Holding(String instrument, BigDecimal shares, Money price, Money total) {
        this.instrument = instrument != null ? instrument : "";
        this.shares = shares != null ? shares : BigDecimal.ZERO;
        this.price = price != null ? price : new Money(BigDecimal.ZERO, Currency.USD);
        this.total = total != null ? total : new Money(BigDecimal.ZERO, Currency.USD);
    }

    public Holding(String instrument, double shares, Money price) {
        this.instrument = instrument != null ? instrument : "";
        this.shares = BigDecimal.valueOf(shares);
        this.price = price != null ? price : new Money(BigDecimal.ZERO, Currency.USD);
        this.total = this.price.multiply(this.shares);
    }

    public Holding(String instrument, int shares, Money price) {
        this(instrument, BigDecimal.valueOf(shares), price);
    }

    public Holding(String instrument, BigDecimal shares, Money price) {
        this.instrument = instrument != null ? instrument : "";
        this.shares = shares != null ? shares : BigDecimal.ZERO;
        this.price = price != null ? price : new Money(BigDecimal.ZERO, Currency.USD);
        this.total = this.price.multiply(this.shares);
    }

    public String getInstrument() {
        return instrument;
    }

    public BigDecimal getShares() {
        return shares;
    }

    public Money getPrice() {
        return price;
    }

    public Money getTotal() {
        return total;
    }

    public Money getTotalInCurrency(Currency targetCurrency, ExchangeRates exchangeRates) {
        if (total.getCurrency().equals(targetCurrency)) {
            return total;
        }

        BigDecimal rate = exchangeRates.getRate(total.getCurrency(), targetCurrency);
        return total.convert(targetCurrency, rate);
    }

    @Override
    public String toString() {
        return String.format("Holding{instrument='%s', shares=%s, price=%s, total=%s}",
                instrument, shares, price, total);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Holding holding = (Holding) obj;
        return Objects.equals(instrument, holding.instrument) &&
                Objects.equals(shares, holding.shares) &&
                Objects.equals(price, holding.price) &&
                Objects.equals(total, holding.total);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instrument, shares, price, total);
    }
}