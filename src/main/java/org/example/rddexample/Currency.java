package org.example.rddexample;

/**
 * Enum representing supported currencies
 */
public enum Currency {
    USD("USD"),
    CHF("CHF"),
    INVALID("INV");

    private final String code;

    Currency(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code;
    }

    public static Currency fromString(String currencyCode) {
        if (currencyCode == null || currencyCode.trim().isEmpty()) {
            return USD; // Default to USD
        }

        try {
            return Currency.valueOf(currencyCode.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            return INVALID;
        }
    }
}
