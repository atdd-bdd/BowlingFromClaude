package org.example.fractions;


import java.util.Scanner;

/**
 * Interactive console application for fraction calculations
 * Implements the behavior specified in the Gherkin scenarios
 */
public class FractionCalculatorApp {

    private final FractionCalculator calculator;
    private final FractionParser parser;
    private final Scanner scanner;

    public FractionCalculatorApp() {
        this.parser = new FractionParser();
        this.calculator = new FractionCalculator(parser);
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        FractionCalculatorApp app = new FractionCalculatorApp();
        app.run();
    }

    public void run() {
        System.out.println("=== Fraction Calculator ===");
        System.out.println("Enter fraction expressions like: 3/4 + 5/8 + 3");
        System.out.println("Supported operations: + (addition), * (multiplication)");
        System.out.println("Supported formats:");
        System.out.println("  • Fractions: 3/4, -5/8");
        System.out.println("  • Whole numbers: 3, -5");
        System.out.println("  • Mixed numbers: 1 1/3, -2 3/4");
        System.out.println("Type 'exit' to quit, 'demo' for examples, 'test' to run scenarios");
        System.out.println();

        boolean running = true;
        while (running) {
            System.out.print("Enter expression: ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Goodbye!");
                running = false;
            } else if (input.equalsIgnoreCase("demo")) {
                runDemo();
            } else if (input.equalsIgnoreCase("test")) {
                runGherkinScenarios();
            } else if (input.isEmpty()) {
                System.out.println("Please enter an expression or 'exit' to quit.");
            } else {
                calculateAndDisplay(input);
            }

            System.out.println();
        }

        scanner.close();
    }

    private void calculateAndDisplay(String expression) {
        try {
            Fraction result = calculator.calculate(expression);
            System.out.println("Result: " + result);

            // Show additional details if it's a reducible fraction
            Fraction reduced = result.reduce();
            if (!result.equals(reduced)) {
                System.out.println("Reduced: " + reduced);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Please check your expression format.");
        }
    }

    private void runDemo() {
        System.out.println("=== Demo Examples ===");

        String[] examples = {
                "3/4 + 5/8",
                "3/4 + 5/8 + 3",
                "11/8 + 3",
                "1/3 + 1/2",
                "1 1/3 + 2 2/3",
                "3 + 2",
                "-3 + 1/4"
        };

        for (String example : examples) {
            try {
                Fraction result = calculator.calculate(example);
                System.out.println(example + " = " + result);
            } catch (Exception e) {
                System.out.println(example + " = ERROR: " + e.getMessage());
            }
        }
    }

    private void runGherkinScenarios() {
        System.out.println("=== Gherkin Scenario Tests ===");

        // Test cases from the Gherkin scenarios
        TestCase[] testCases = {
                new TestCase("3/4 + 5/8 + 3", "35/8", "Fraction Calculator scenario"),
                new TestCase("3/4 + 5/8", "11/8", "Basic expression"),
                new TestCase("11/8 + 3", "35/8", "Mixed calculation"),
                new TestCase("3", "3", "Whole number"),
                new TestCase("1/3 + 1/3", "2/3", "Same denominator"),
                new TestCase("1/3 + 2/3", "1", "Whole number result"),
                new TestCase("1/3 + 1/2", "5/6", "Different denominators"),
                new TestCase("1/6 + 1/8", "7/24", "Complex reduction"),
                new TestCase("1/4 + 1/4", "1/2", "Simple reduction")
        };

        int passed = 0;
        int total = testCases.length;

        for (TestCase test : testCases) {
            try {
                Fraction result = calculator.calculate(test.expression);
                Fraction expected = parser.parse(test.expectedResult);

                if (result.equals(expected)) {
                    System.out.println("✓ PASS: " + test.expression + " = " + result + " (" + test.description + ")");
                    passed++;
                } else {
                    System.out.println("✗ FAIL: " + test.expression + " = " + result +
                            ", expected " + expected + " (" + test.description + ")");
                }
            } catch (Exception e) {
                System.out.println("✗ ERROR: " + test.expression + " - " + e.getMessage() +
                        " (" + test.description + ")");
            }
        }

        System.out.println();
        System.out.println("Test Results: " + passed + "/" + total + " passed");

        if (passed == total) {
            System.out.println("All Gherkin scenarios passed!");
        } else {
            System.out.println("Some scenarios failed - check implementation");
        }
    }

    /**
     * Test case for validating Gherkin scenarios
     */
    private static class TestCase {
        final String expression;
        final String expectedResult;
        final String description;

        TestCase(String expression, String expectedResult, String description) {
            this.expression = expression;
            this.expectedResult = expectedResult;
            this.description = description;
        }
    }

    /**
     * Calculate a single expression and return the result (for programmatic use)
     */
    public Fraction calculateExpression(String expression) {
        return calculator.calculate(expression);
    }

    /**
     * Parse a single fraction from display format (for programmatic use)
     */
    public Fraction parseFraction(String display) {
        return parser.parse(display);
    }
}