package org.example.rddexampleupdated;

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
        demonstrateNewValueObjects();
    }

    public static void runBasicScenario() {
        System.out.println("=== Basic Scenario ===");

        // Create holdings using convenience constructors
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

        // Create holdings using value objects
        List<Holding> holdings = Arrays.asList(
                new Holding(new Instrument("IBM"), new Shares(1000), new Money(25, Currency.USD)),
                new Holding(new Instrument("Novartis"), new Shares(400), new Money(150, Currency.CHF))
        );

        // Convert holdings to show USD equivalents
        List<HoldingWithConversion> convertedHoldings =
                PortfolioCalculator.convertHoldings(holdings, Currency.USD, exchangeRates);

        System.out.println("Holdings with USD conversion:");
        for (HoldingWithConversion holding : convertedHoldings) {
            System.out.printf("%-10s | %8s | %10s | %12s | %12s%n",
                    holding.getInstrument(),
                    holding.getShares(),
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

        // Create holdings with decimal values using value objects
        List<Holding> holdings = Arrays.asList(
                new Holding(Instrument.of("IBM"), Shares.of("1000.1"), new Money(new BigDecimal("25.20"), Currency.USD)),
                new Holding(Instrument.of("IBM"), Shares.of(100), new Money(40, Currency.CHF)),
                new Holding(Instrument.of("Novartis"), Shares.of(400), new Money(150, Currency.CHF))
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

    public static void demonstrateNewValueObjects() {
        System.out.println("=== Demonstrating New Value Objects ===");

        // Creating Instruments
        Instrument ibm = new Instrument("ibm");  // Will be normalized to "IBM"
        Instrument novartis = Instrument.of("Novartis");
        System.out.println("Instruments: " + ibm + ", " + novartis);

        // Creating Shares
        Shares shares1 = new Shares(1000);
        Shares shares2 = Shares.of(250.5);
        Shares shares3 = Shares.of("1,000.75");  // Supports comma formatting
        System.out.println("Shares: " + shares1 + ", " + shares2 + ", " + shares3);

        // Shares arithmetic
        Shares totalShares = shares1.add(shares2);
        System.out.println("Combined shares: " + totalShares);

        // Shares with Money calculation
        Money price = new Money(50, Currency.USD);
        Money value = shares1.multiply(price);
        System.out.println("Value of " + shares1 + " shares at " + price + " = " + value);

        // Creating holdings with value objects
        Holding holding = new Holding(ibm, shares1, price);
        System.out.println("Complete holding: " + holding);

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