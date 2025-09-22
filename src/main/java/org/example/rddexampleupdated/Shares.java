package org.example.rddexampleupdated;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Represents a quantity of shares in a financial instrument
 */
public class Shares {
    private final BigDecimal quantity;

    public Shares(BigDecimal quantity) {
        if (quantity == null) {
            throw new IllegalArgumentException("Shares quantity cannot be null");
        }
        if (quantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Shares quantity cannot be negative");
        }
        this.quantity = quantity;
    }

    public Shares(double quantity) {
        this(BigDecimal.valueOf(quantity));
    }

    public Shares(int quantity) {
        this(BigDecimal.valueOf(quantity));
    }

    public Shares(String quantity) {
        if (quantity == null || quantity.trim().isEmpty()) {
            throw new IllegalArgumentException("Shares quantity string cannot be null or empty");
        }
        try {
            this.quantity = new BigDecimal(quantity.trim().replaceAll(",", ""));
            if (this.quantity.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Shares quantity cannot be negative");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid shares quantity: " + quantity);
        }
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public Shares add(Shares other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot add null shares");
        }
        return new Shares(this.quantity.add(other.quantity));
    }

    public Money multiply(Money price) {
        if (price == null) {
            throw new IllegalArgumentException("Cannot multiply by null price");
        }
        return price.multiply(this.quantity);
    }

    public static Shares of(BigDecimal quantity) {
        return new Shares(quantity);
    }

    public static Shares of(double quantity) {
        return new Shares(quantity);
    }

    public static Shares of(int quantity) {
        return new Shares(quantity);
    }

    public static Shares of(String quantity) {
        return new Shares(quantity);
    }

    @Override
    public String toString() {
        // Format without decimal places for whole numbers
        if (quantity.compareTo(new BigDecimal(quantity.intValue())) == 0) {
            return String.valueOf(quantity.intValue());
        } else {
            return quantity.setScale(2, RoundingMode.HALF_UP).toPlainString();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Shares shares = (Shares) obj;
        return Objects.equals(quantity.setScale(2, RoundingMode.HALF_UP),
                shares.quantity.setScale(2, RoundingMode.HALF_UP));
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity.setScale(2, RoundingMode.HALF_UP));
    }
}