package org.example.dollar;// MoneyState.java - Fixed version
// MoneyState.java - Pattern-based error message determination
import java.math.BigDecimal;

public class MoneyState {
    private Dollar total;
    private int count;

    public MoneyState() {
        this.total = Dollar.of(BigDecimal.ZERO);
        this.count = 0;
    }

    public MoneyState(Dollar total, int count) {
        this.total = total;
        this.count = count;
    }

    public MoneyState(String totalAmount, int count) {
        this.total = Dollar.of(totalAmount);
        this.count = count;
    }

    // Add money - returns new state and any error message
    public MoneyTransactionResult addMoney(String amountInput) {
        DollarValidationResult validation = Dollar.validate(amountInput);

        if (!validation.isValid()) {
            String message = determineErrorMessage(amountInput, validation.getMessage());
            return new MoneyTransactionResult(this, message);
        }

        try {
            Dollar amount = Dollar.of(amountInput);
            Dollar newTotal = total.add(amount);
            MoneyState newState = new MoneyState(newTotal, count + 1);
            return new MoneyTransactionResult(newState, "");
        } catch (Exception e) {
            return new MoneyTransactionResult(this, "Invalid format");
        }
    }

    private String determineErrorMessage(String input, String validationMessage) {
        String trimmed = input.trim();

        // Check if this is one of the specific validation patterns that should
        // return their specific message rather than "Invalid format"

        // Decimal validation - single digit after decimal
        if (trimmed.matches(".*\\d\\.\\d([^\\d].*|$)")) {
            return validationMessage; // "Must be either zero or two digits"
        }

        // Unmatched parentheses
        if (hasUnmatchedParentheses(trimmed)) {
            return validationMessage; // "Parathensis must match"
        }

        // Double negative patterns
        if (trimmed.matches(".*-.*\\(.*\\$.*\\).*") || trimmed.matches(".*\\(.*-.*\\$.*\\).*")) {
            return validationMessage; // "Double negative"
        }

        // Comma placement issues
        if (trimmed.contains(",") && !hasValidCommaPlacement(trimmed)) {
            return validationMessage; // "Commas must be every three digits"
        }

        // Single letter inputs (like "A")
        if (trimmed.matches("^[A-Za-z]$")) {
            return validationMessage; // "Non-numeric"
        }

        // Contains letters mixed with valid dollar characters
        if (trimmed.matches(".*[A-Za-z].*") && trimmed.matches(".*[\\$\\d].*")) {
            return validationMessage; // "Contains non-numeric"
        }

        // Characters in incorrect order (dollar sign after digits, parentheses mixed with numbers)
        if (hasCharactersInWrongOrder(trimmed)) {
            return validationMessage; // "Characters in incorrect order"
        }

        // For any other invalid input pattern, return generic message
        return "Invalid format";
    }

    private boolean hasUnmatchedParentheses(String input) {
        int openCount = 0;
        for (char c : input.toCharArray()) {
            if (c == '(') openCount++;
            else if (c == ')') openCount--;
            if (openCount < 0) return true;
        }
        return openCount != 0;
    }

    private boolean hasValidCommaPlacement(String input) {
        // Extract just the numeric part with commas
        String numeric = input.replaceAll("[\\$\\(\\)\\-]", "");

        // Split by decimal point if present
        String integerPart = numeric;
        if (numeric.contains(".")) {
            integerPart = numeric.split("\\.")[0];
        }

        // If no commas, it's valid
        if (!integerPart.contains(",")) {
            return true;
        }

        // Check if all commas or no commas
        String withoutCommas = integerPart.replace(",", "");
        if (withoutCommas.length() <= 3) {
            return false; // No need for commas in numbers <= 999
        }

        // Validate comma pattern: should be every 3 digits from right
        String[] parts = integerPart.split(",");

        // First part can be 1-3 digits
        if (parts[0].length() == 0 || parts[0].length() > 3) {
            return false;
        }

        // All other parts must be exactly 3 digits
        for (int i = 1; i < parts.length; i++) {
            if (parts[i].length() != 3) {
                return false;
            }
        }

        return true;
    }

    private boolean hasCharactersInWrongOrder(String input) {
        // Check if $ appears after digits (not at start)
        if (input.matches(".*\\d+.*\\$.*")) {
            return true;
        }

        // Check if parentheses appear in middle of string with other characters
        if (input.matches(".*\\d.*\\(.*\\).*\\d.*")) {
            return true;
        }

        // Check for mixed special characters in wrong positions
        if (input.matches(".*\\d.*[\\$\\(\\)].*\\d.*[\\$\\(\\)].*")) {
            return true;
        }

        return false;
    }

    // Getters
    public Dollar getTotal() { return total; }
    public int getCount() { return count; }

    @Override
    public String toString() {
        return String.format("Total: %s, Count: %d", total, count);
    }
}