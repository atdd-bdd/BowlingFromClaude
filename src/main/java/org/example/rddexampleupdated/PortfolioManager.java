package org.example.rddexampleupdated;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Interactive console application for managing a portfolio
 * Demonstrates the RDD Portfolio System with menu-driven interface
 */
public class PortfolioManager {

    private final List<Holding> holdings;
    private final ExchangeRates exchangeRates;
    private final Scanner scanner;

    public PortfolioManager() {
        this.holdings = new ArrayList<>();
        this.exchangeRates = new ExchangeRates();
        this.scanner = new Scanner(System.in);

        // Initialize with some default exchange rates
        initializeDefaultRates();
    }

    public static void main(String[] args) {
        PortfolioManager manager = new PortfolioManager();
        manager.run();
    }

    public void run() {
        System.out.println("=== RDD Portfolio Management System ===");
        System.out.println("Welcome to your personal portfolio manager!");
        System.out.println();

        boolean running = true;
        while (running) {
            displayMenu();

            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                System.out.println();

                switch (choice) {
                    case 1:
                        addHolding();
                        break;
                    case 2:
                        updateExchangeRate();
                        break;
                    case 3:
                        printHoldings();
                        break;
                    case 4:
                        printPortfolioSummary();
                        break;
                    case 5:
                        printHoldingsWithConversion();
                        break;
                    case 6:
                        removeHolding();
                        break;
                    case 7:
                        viewExchangeRates();
                        break;
                    case 8:
                        System.out.println("Thank you for using Portfolio Manager. Goodbye!");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please select 1-8.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number (1-8).");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
                System.out.println();
            }
        }

        scanner.close();
    }

    private void displayMenu() {
        System.out.println("=== PORTFOLIO MENU ===");
        System.out.println("1. Add Holding");
        System.out.println("2. Update Exchange Rate");
        System.out.println("3. View Holdings");
        System.out.println("4. Portfolio Summary");
        System.out.println("5. Holdings with Currency Conversion");
        System.out.println("6. Remove Holding");
        System.out.println("7. View Exchange Rates");
        System.out.println("8. Exit");
        System.out.print("Choose an option (1-8): ");
    }

    private void addHolding() {
        System.out.println("=== Add New Holding ===");

        try {
            System.out.print("Instrument symbol (e.g., IBM, AAPL): ");
            String symbol = scanner.nextLine().trim();
            if (symbol.isEmpty()) {
                System.out.println("Error: Instrument symbol cannot be empty.");
                return;
            }

            System.out.print("Number of shares (e.g., 1000, 250.5): ");
            String sharesInput = scanner.nextLine().trim();

            System.out.print("Price per share (e.g., 25.50): ");
            String priceInput = scanner.nextLine().trim();

            System.out.print("Currency (USD, CHF): ");
            String currencyInput = scanner.nextLine().trim().toUpperCase();

            // Create value objects
            Instrument instrument = new Instrument(symbol);
            Shares shares = new Shares(sharesInput);
            Currency currency = Currency.fromString(currencyInput);

            if (currency == Currency.INVALID) {
                System.out.println("Error: Invalid currency '" + currencyInput + "'. Supported: USD, CHF");
                return;
            }

            Money price = new Money(new BigDecimal(priceInput), currency);
            Holding holding = new Holding(instrument, shares, price);

            holdings.add(holding);

            System.out.println("Successfully added holding:");
            System.out.println(holding);

        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid number format. Please use decimal numbers (e.g., 123.45)");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void updateExchangeRate() {
        System.out.println("=== Update Exchange Rate ===");

        try {
            System.out.print("From currency (USD, CHF): ");
            String fromInput = scanner.nextLine().trim().toUpperCase();

            System.out.print("To currency (USD, CHF): ");
            String toInput = scanner.nextLine().trim().toUpperCase();

            System.out.print("Exchange rate (e.g., 1.52): ");
            String rateInput = scanner.nextLine().trim();

            Currency fromCurrency = Currency.fromString(fromInput);
            Currency toCurrency = Currency.fromString(toInput);

            if (fromCurrency == Currency.INVALID) {
                System.out.println("Error: Invalid 'from' currency '" + fromInput + "'");
                return;
            }

            if (toCurrency == Currency.INVALID) {
                System.out.println("Error: Invalid 'to' currency '" + toInput + "'");
                return;
            }

            if (fromCurrency == toCurrency) {
                System.out.println("Error: From and To currencies cannot be the same");
                return;
            }

            BigDecimal rate = new BigDecimal(rateInput);
            if (rate.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Error: Exchange rate must be positive");
                return;
            }

            exchangeRates.addRate(fromCurrency, toCurrency, rate);

            System.out.println("Successfully updated exchange rate:");
            System.out.println(fromCurrency + " to " + toCurrency + " = " + rate);
            System.out.println("(Inverse rate " + toCurrency + " to " + fromCurrency + " = " +
                    BigDecimal.ONE.divide(rate, 10, BigDecimal.ROUND_HALF_UP) + ")");

        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid rate format. Please use decimal numbers (e.g., 1.52)");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void printHoldings() {
        System.out.println("=== Current Holdings ===");

        if (holdings.isEmpty()) {
            System.out.println("No holdings found. Use option 1 to add holdings.");
            return;
        }

        System.out.printf("%-12s | %-10s | %-12s | %-15s%n",
                "Instrument", "Shares", "Price", "Total Value");
        System.out.println("---------|----------|------------|---------------");

        for (int i = 0; i < holdings.size(); i++) {
            Holding holding = holdings.get(i);
            System.out.printf("%-12s | %-10s | %-12s | %-15s%n",
                    holding.getInstrument().getSymbol(),
                    holding.getShares(),
                    holding.getPrice(),
                    holding.getTotal());
        }
    }

    private void printPortfolioSummary() {
        System.out.println("=== Portfolio Summary ===");

        if (holdings.isEmpty()) {
            System.out.println("No holdings found.");
            return;
        }

        printHoldings();
        System.out.println();

        // Show totals in different currencies
        for (Currency currency : new Currency[]{Currency.USD, Currency.CHF}) {
            try {
                Total total = PortfolioCalculator.calculateTotal(holdings, currency, exchangeRates);
                System.out.println("Total Portfolio Value in " + currency + ": " + total.getOverallTotal());
            } catch (Exception e) {
                System.out.println("Cannot calculate total in " + currency + ": " + e.getMessage());
            }
        }
    }

    private void printHoldingsWithConversion() {
        System.out.println("=== Holdings with Currency Conversion ===");

        if (holdings.isEmpty()) {
            System.out.println("No holdings found.");
            return;
        }

        System.out.print("Convert to currency (USD, CHF): ");
        String currencyInput = scanner.nextLine().trim().toUpperCase();
        Currency targetCurrency = Currency.fromString(currencyInput);

        if (targetCurrency == Currency.INVALID) {
            System.out.println("Error: Invalid currency '" + currencyInput + "'");
            return;
        }

        try {
            List<HoldingWithConversion> convertedHoldings =
                    PortfolioCalculator.convertHoldings(holdings, targetCurrency, exchangeRates);

            System.out.printf("%-12s | %-10s | %-12s | %-15s | %-15s%n",
                    "Instrument", "Shares", "Price", "Original Total", "Converted Total");
            System.out.println("---------|----------|------------|---------------|---------------");

            for (HoldingWithConversion holding : convertedHoldings) {
                System.out.printf("%-12s | %-10s | %-12s | %-15s | %-15s%n",
                        holding.getInstrument(),
                        holding.getShares(),
                        holding.getPrice(),
                        holding.getTotal(),
                        holding.getTotalInTargetCurrency());
            }

            Total total = PortfolioCalculator.calculateTotal(holdings, targetCurrency, exchangeRates);
            System.out.println();
            System.out.println("Overall Portfolio Total: " + total.getOverallTotal());

        } catch (Exception e) {
            System.out.println("Error converting to " + targetCurrency + ": " + e.getMessage());
            System.out.println("You may need to add the required exchange rates first (option 2).");
        }
    }

    private void removeHolding() {
        System.out.println("=== Remove Holding ===");

        if (holdings.isEmpty()) {
            System.out.println("No holdings to remove.");
            return;
        }

        printHoldings();
        System.out.println();

        try {
            System.out.print("Enter holding number to remove (1-" + holdings.size() + "): ");
            int index = Integer.parseInt(scanner.nextLine().trim()) - 1;

            if (index < 0 || index >= holdings.size()) {
                System.out.println("Error: Invalid holding number.");
                return;
            }

            Holding removed = holdings.remove(index);
            System.out.println("Successfully removed holding: " + removed.getInstrument().getSymbol());

        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid number.");
        }
    }

    private void viewExchangeRates() {
        System.out.println("=== Current Exchange Rates ===");

        Currency[] currencies = {Currency.USD, Currency.CHF};

        System.out.printf("%-8s | %-8s | %-10s%n", "From", "To", "Rate");
        System.out.println("---------|----------|----------");

        for (Currency from : currencies) {
            for (Currency to : currencies) {
                if (from != to) {
                    try {
                        BigDecimal rate = exchangeRates.getRate(from, to);
                        System.out.printf("%-8s | %-8s | %-10s%n", from, to, rate);
                    } catch (Exception e) {
                        System.out.printf("%-8s | %-8s | %-10s%n", from, to, "Not set");
                    }
                }
            }
        }
    }

    private void initializeDefaultRates() {
        // Initialize with some common exchange rates
        try {
            exchangeRates.addRate(Currency.CHF, Currency.USD, 1.08); // Example rate
            exchangeRates.addRate(Currency.USD, Currency.CHF, 0.93); // Will be auto-calculated as inverse
        } catch (Exception e) {
            // Ignore if rates can't be set
        }
    }
}