package org.example.fractions;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service for calculating fraction expressions
 * Handles expressions like "3/4 + 5/8 + 3" and "11/8 + 3"
 */
public class FractionCalculator {

    private final FractionParser parser;

    // Pattern to match fraction/number followed by operator followed by fraction/number
    // Supports: numbers, fractions, mixed numbers with +/* operators
    private static final Pattern EXPRESSION_PATTERN =
            Pattern.compile("([+-]?(?:\\d+\\s+)?\\d+(?:/\\d+)?)\\s*([+*])\\s*([+-]?(?:\\d+\\s+)?\\d+(?:/\\d+)?)");

    public FractionCalculator() {
        this.parser = new FractionParser();
    }

    // Constructor injection for testing
    public FractionCalculator(FractionParser parser) {
        if (parser == null) {
            throw new IllegalArgumentException("FractionParser cannot be null");
        }
        this.parser = parser;
    }

    /**
     * Calculate the result of a fraction expression
     *
     * Supported formats:
     * - "3/4 + 5/8"
     * - "3/4 + 5/8 + 3"
     * - "11/8 + 3"
     * - "1 1/3 + 2 2/3"
     *
     * @param expression the mathematical expression to evaluate
     * @return the calculated result as a Fraction
     */
    public Fraction calculate(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            throw new IllegalArgumentException("Expression cannot be null or empty");
        }

        String trimmed = expression.trim();

        // Handle single fraction/number (no operators)
        if (!containsOperators(trimmed)) {
            return parser.parse(trimmed);
        }

        // Parse and evaluate the expression left-to-right
        return evaluateLeftToRight(trimmed);
    }

    private boolean containsOperators(String expression) {
        return expression.contains("+") || expression.contains("*");
    }

    private Fraction evaluateLeftToRight(String expression) {
        // Find all components (fractions/numbers and operators)
        List<String> tokens = tokenize(expression);

        if (tokens.size() < 3 || tokens.size() % 2 == 0) {
            throw new IllegalArgumentException("Invalid expression format: " + expression);
        }

        // Start with the first operand
        Fraction result = parser.parse(tokens.get(0));

        // Process each operator and operand pair
        for (int i = 1; i < tokens.size(); i += 2) {
            String operatorSymbol = tokens.get(i);
            String operandStr = tokens.get(i + 1);

            Operator operator = Operator.fromSymbol(operatorSymbol);
            Fraction operand = parser.parse(operandStr);

            result = operator.apply(result, operand);
        }

        return result;
    }

    private List<String> tokenize(String expression) {
        List<String> tokens = new ArrayList<>();

        // Split on operators while keeping the operators
        String[] parts = expression.split("(?=[+*])|(?<=[+*])");

        for (String part : parts) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                tokens.add(trimmed);
            }
        }

        return tokens;
    }

    /**
     * Validate that an expression has correct syntax
     */
    public boolean isValidExpression(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            return false;
        }

        try {
            calculate(expression);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Parse an expression into its components for analysis
     */
    public ExpressionComponents parseComponents(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            throw new IllegalArgumentException("Expression cannot be null or empty");
        }

        String trimmed = expression.trim();

        // Handle simple binary expressions like "3/4 + 5/8"
        Matcher matcher = EXPRESSION_PATTERN.matcher(trimmed);
        if (matcher.matches()) {
            String leftOperand = matcher.group(1);
            String operator = matcher.group(2);
            String rightOperand = matcher.group(3);

            return new ExpressionComponents(
                    parser.parse(leftOperand),
                    Operator.fromSymbol(operator),
                    parser.parse(rightOperand)
            );
        }

        throw new IllegalArgumentException("Cannot parse expression components: " + expression);
    }

    /**
     * Value object representing the components of a binary expression
     */
    public static class ExpressionComponents {
        private final Fraction leftOperand;
        private final Operator operator;
        private final Fraction rightOperand;

        public ExpressionComponents(Fraction leftOperand, Operator operator, Fraction rightOperand) {
            if (leftOperand == null || operator == null || rightOperand == null) {
                throw new IllegalArgumentException("Expression components cannot be null");
            }
            this.leftOperand = leftOperand;
            this.operator = operator;
            this.rightOperand = rightOperand;
        }

        public Fraction getLeftOperand() {
            return leftOperand;
        }

        public Operator getOperator() {
            return operator;
        }

        public Fraction getRightOperand() {
            return rightOperand;
        }

        public Fraction evaluate() {
            return operator.apply(leftOperand, rightOperand);
        }

        @Override
        public String toString() {
            return leftOperand + " " + operator + " " + rightOperand;
        }
    }
}