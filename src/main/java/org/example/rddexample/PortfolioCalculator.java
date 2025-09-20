package org.example.rddexample;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Calculator for portfolio operations including totaling holdings and currency conversions
 */
public class PortfolioCalculator {

    /**
     * Calculate total value of holdings in a specific currency
     */
    public static Total calculateTotal(List<Holding> holdings, Currency targetCurrency, ExchangeRates exchangeRates) {
        if (holdings == null || holdings.isEmpty()) {
            return new Total(new Money(BigDecimal.ZERO, targetCurrency));
        }

        Money total = new Money(BigDecimal.ZERO, targetCurrency);

        for (Holding holding : holdings) {
            Money holdingTotal = holding.getTotal();

            if (holdingTotal.getCurrency().equals(targetCurrency)) {
                total = total.add(holdingTotal);
            } else {
                BigDecimal rate = exchangeRates.getRate(holdingTotal.getCurrency(), targetCurrency);
                Money convertedTotal = holdingTotal.convert(targetCurrency, rate);
                total = total.add(convertedTotal);
            }
        }

        return new Total(total);
    }

    /**
     * Calculate total value of holdings (assumes USD if mixed currencies)
     */
    public static Total calculateTotal(List<Holding> holdings) {
        return calculateTotal(holdings, Currency.USD, new ExchangeRates());
    }

    /**
     * Convert holdings to show totals in target currency
     */
    public static List<HoldingWithConversion> convertHoldings(List<Holding> holdings, Currency targetCurrency, ExchangeRates exchangeRates) {
        List<HoldingWithConversion> convertedHoldings = new ArrayList<>();

        for (Holding holding : holdings) {
            Money totalInTargetCurrency = holding.getTotalInCurrency(targetCurrency, exchangeRates);
            convertedHoldings.add(new HoldingWithConversion(holding, totalInTargetCurrency));
        }

        return convertedHoldings;
    }

    /**
     * Add two money amounts, converting to output currency if needed
     */
    public static Money addWithConversion(Money addend1, Money addend2, Currency outputCurrency, ExchangeRates exchangeRates) {
        Money converted1 = addend1;
        Money converted2 = addend2;

        if (!addend1.getCurrency().equals(outputCurrency)) {
            BigDecimal rate1 = exchangeRates.getRate(addend1.getCurrency(), outputCurrency);
            converted1 = addend1.convert(outputCurrency, rate1);
        }

        if (!addend2.getCurrency().equals(outputCurrency)) {
            BigDecimal rate2 = exchangeRates.getRate(addend2.getCurrency(), outputCurrency);
            converted2 = addend2.convert(outputCurrency, rate2);
        }

        return converted1.add(converted2);
    }

    /**
     * Multiply quantity by money amount
     */
    public static Money multiply(BigDecimal quantity, Money money) {
        return money.multiply(quantity);
    }

    /**
     * Multiply quantity by money amount
     */
    public static Money multiply(int quantity, Money money) {
        return money.multiply(quantity);
    }

    /**
     * Multiply quantity by money amount
     */
    public static Money multiply(double quantity, Money money) {
        return money.multiply(quantity);
    }
}

