package org.example.rddexample;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Represents a monetary amount with currency
 */
public class Money {
    private final BigDecimal amount;
    private final Currency currency;

    public Money(BigDecimal amount, Currency currency) {
        this.amount = amount != null ? amount : BigDecimal.ZERO;
        this.currency = currency != null ? currency : Currency.USD;
    }

    public Money(double amount, Currency currency) {
        this(BigDecimal.valueOf(amount), currency);
    }

    public Money(String display) {
        BigDecimal parsedAmount;
        Currency parsedCurrency;

        if (display == null || display.trim().isEmpty()) {
            parsedAmount = BigDecimal.ZERO;
            parsedCurrency = Currency.USD;
        } else {
            display = display.trim();
            String[] parts = display.split("\\s+");

            if (parts.length == 1) {
                // Only amount, assume USD
                try {
                    parsedAmount = new BigDecimal(parts[0].replaceAll(",", ""));
                    parsedCurrency = Currency.USD;
                } catch (NumberFormatException e) {
                    parsedAmount = BigDecimal.ZERO;
                    parsedCurrency = Currency.INVALID;
                }
            } else if (parts.length == 2) {
                // Amount and currency
                try {
                    parsedAmount = new BigDecimal(parts[0].replaceAll(",", ""));
                    parsedCurrency = Currency.valueOf(parts[1]);
                } catch (Exception e) {
                    parsedAmount = BigDecimal.ZERO;
                    parsedCurrency = Currency.INVALID;
                }
            } else {
                parsedAmount = BigDecimal.ZERO;
                parsedCurrency = Currency.INVALID;
            }
        }

        this.amount = parsedAmount;
        this.currency = parsedCurrency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot add different currencies without exchange rate");
        }
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money multiply(BigDecimal quantity) {
        return new Money(this.amount.multiply(quantity), this.currency);
    }

    public Money multiply(int quantity) {
        return multiply(BigDecimal.valueOf(quantity));
    }

    public Money multiply(double quantity) {
        return multiply(BigDecimal.valueOf(quantity));
    }

    public Money convert(Currency targetCurrency, BigDecimal exchangeRate) {
        if (this.currency.equals(targetCurrency)) {
            return this;
        }
        BigDecimal convertedAmount = this.amount.multiply(exchangeRate);
        return new Money(convertedAmount, targetCurrency);
    }

    @Override
    public String toString() {
        // For all currencies, format without decimal places for whole numbers
        if (amount.compareTo(new BigDecimal(amount.intValue())) == 0) {
            return String.valueOf(amount.intValue()) + " " + currency;
        } else {
            if (currency == Currency.INVALID) {
                return amount.toPlainString() + " " + currency;
            } else {
                return String.format("%.2f %s", amount, currency);
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Money money = (Money) obj;
        return Objects.equals(amount.setScale(2, RoundingMode.HALF_UP),
                money.amount.setScale(2, RoundingMode.HALF_UP)) &&
                currency == money.currency;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount.setScale(2, RoundingMode.HALF_UP), currency);
    }
}