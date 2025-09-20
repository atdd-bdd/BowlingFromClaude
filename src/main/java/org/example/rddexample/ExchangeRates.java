package org.example.rddexample;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Manages exchange rates between currencies
 */
public class ExchangeRates {
    private final Map<CurrencyPair, BigDecimal> rates;

    public ExchangeRates() {
        this.rates = new HashMap<>();
    }

    public void addRate(Currency from, Currency to, BigDecimal rate) {
        if (from == null || to == null || rate == null) {
            throw new IllegalArgumentException("Currency and rate cannot be null");
        }

        CurrencyPair pair = new CurrencyPair(from, to);
        rates.put(pair, rate);

        // Add inverse rate
        if (!rate.equals(BigDecimal.ZERO)) {
            CurrencyPair inversePair = new CurrencyPair(to, from);
            rates.put(inversePair, BigDecimal.ONE.divide(rate, 10, BigDecimal.ROUND_HALF_UP));
        }
    }

    public void addRate(Currency from, Currency to, double rate) {
        addRate(from, to, BigDecimal.valueOf(rate));
    }

    public BigDecimal getRate(Currency from, Currency to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Currencies cannot be null");
        }

        if (from.equals(to)) {
            return BigDecimal.ONE;
        }

        CurrencyPair pair = new CurrencyPair(from, to);
        BigDecimal rate = rates.get(pair);

        if (rate == null) {
            throw new IllegalArgumentException("Exchange rate not found for " + from + " to " + to);
        }

        return rate;
    }

    public Money convert(Money money, Currency targetCurrency) {
        if (money == null || targetCurrency == null) {
            throw new IllegalArgumentException("Money and target currency cannot be null");
        }

        BigDecimal rate = getRate(money.getCurrency(), targetCurrency);
        return money.convert(targetCurrency, rate);
    }

    private static class CurrencyPair {
        private final Currency from;
        private final Currency to;

        public CurrencyPair(Currency from, Currency to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            CurrencyPair that = (CurrencyPair) obj;
            return from == that.from && to == that.to;
        }

        @Override
        public int hashCode() {
            return Objects.hash(from, to);
        }

        @Override
        public String toString() {
            return from + " -> " + to;
        }
    }
}