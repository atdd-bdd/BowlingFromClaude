package org.example.rddexample;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * Example usage demonstrating the portfolio calculation system
 */
public class ExampleUsage {

    public static void main(String[] args) {
        // Example from the Gherkin scenarios
        runBasicScenario();
        runCurrencyConversionScenario();
        runExpandedScenario();
    }

    public static void runBasicScenario() {
        System.out.println("=== Basic Scenario ===");

        // Create holdings
        List<Holding> holdings = Arrays.asList(
                new Holding("IBM", 1000, new Money(25, Currency.USD)),
                new Holding("Novartis", 400, new Money(150, Currency.CHF))
        );

        // Display holdings
        for (Holding holding : holdings) {
            System.out.println(holding);
        }

        // Calculate total (this won't work without exchange rates for mixed currencies)
        try {
            Total total = PortfolioCalculator.calculateTotal(holdings);
            System.out.println("Total: " + total.getOverallTotal());
        } catch (Exception e) {
            System.out.println("Cannot calculate total without exchange rates for mixed currencies");
        }

        System.out.println();
    }

    public static void runCurrencyConversionScenario() {
        System.out.println("=== Currency Conversion Scenario ===");

        // Create exchange rates
        ExchangeRates exchangeRates = new ExchangeRates();
        exchangeRates.addRate(Currency.CHF, Currency.USD, 1.5);

        // Create holdings
        List<Holding> holdings = Arrays.asList(
                new Holding("IBM", 1000, new Money(25, Currency.USD)),
                new Holding("Novartis", 400, new Money(150, Currency.CHF))
        );

        // Convert holdings to show USD equivalents
        List<HoldingWithConversion> convertedHoldings =
                PortfolioCalculator.convertHoldings(holdings, Currency.USD, exchangeRates);

        System.out.println("Holdings with USD conversion:");
        for (HoldingWithConversion holding : convertedHoldings) {
            System.out.printf("%-10s | %8s | %10s | %12s | %12s%n",
                    holding.getInstrument(),
                    holding.getHolding().getShares(),
                    holding.getPrice(),
                    holding.getTotal(),
                    holding.getTotalInTargetCurrency()
            );
        }

        // Calculate total in USD
        Total total = PortfolioCalculator.calculateTotal(holdings, Currency.USD, exchangeRates);
        System.out.println("Overall Total: " + total.getOverallTotal());

        System.out.println();
    }

    public static void runExpandedScenario() {
        System.out.println("=== Expanded Scenario ===");

        // Create exchange rates
        ExchangeRates exchangeRates = new ExchangeRates();
        exchangeRates.addRate(Currency.CHF, Currency.USD, 1.52);

        // Create holdings with decimal values
        List<Holding> holdings = Arrays.asList(
                new Holding("IBM", new BigDecimal("1000.1"), new Money(new BigDecimal("25.20"), Currency.USD)),
                new Holding("IBM", 100, new Money(40, Currency.CHF)),
                new Holding("Novartis", 400, new Money(150, Currency.CHF))
        );

        // Display holdings
        System.out.println("Holdings:");
        for (Holding holding : holdings) {
            System.out.println(holding);
        }

        // Calculate total in USD
        Total total = PortfolioCalculator.calculateTotal(holdings, Currency.USD, exchangeRates);
        System.out.println("Overall Total: " + total.getOverallTotal());

        System.out.println();
    }

    public static void demonstrateMoneyParsing() {
        System.out.println("=== Money Parsing Examples ===");

        // Test money parsing from display strings
        String[] testStrings = {
                "25000 USD",
                "60000 CHF",
                "25000 XXX",  // Invalid currency
                "25000"       // Missing currency (should default to USD)
        };

        for (String testString : testStrings) {
            Money money = new Money(testString);
            System.out.printf("'%s' -> Amount: %s, Currency: %s%n",
                    testString, money.getAmount(), money.getCurrency());
        }

        System.out.println();
    }
}