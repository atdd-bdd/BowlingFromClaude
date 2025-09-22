package org.example.fractions;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service for parsing display strings into Fraction objects
 * Handles: whole numbers, simple fractions, mixed numbers, negative numbers
 */
public class FractionParser {

    // Pattern for mixed numbers like "1 1/3" or "-2 3/4"
    private static final Pattern MIXED_NUMBER_PATTERN =
            Pattern.compile("^(-?\\d+)\\s+(\\d+)/(\\d+)$");

    // Pattern for simple fractions like "3/4" or "-5/8"
    private static final Pattern SIMPLE_FRACTION_PATTERN =
            Pattern.compile("^(-?\\d+)/(-?\\d+)$");

    // Pattern for whole numbers like "3" or "-5"
    private static final Pattern WHOLE_NUMBER_PATTERN =
            Pattern.compile("^(-?\\d+)$");

    /**
     * Parse a display string into a Fraction
     *
     * Supported formats:
     * - Whole numbers: "3", "-5"
     * - Simple fractions: "3/4", "-5/8"
     * - Mixed numbers: "1 1/3", "-2 3/4"
     */
    public Fraction parse(String display) {
        if (display == null) {
            throw new IllegalArgumentException("Display string cannot be null");
        }

        String trimmed = display.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Display string cannot be empty");
        }

        // Try mixed numbers first (most complex pattern)
        Matcher mixedMatcher = MIXED_NUMBER_PATTERN.matcher(trimmed);
        if (mixedMatcher.matches()) {
            return parseMixedNumber(mixedMatcher);
        }

        // Try simple fractions
        Matcher fractionMatcher = SIMPLE_FRACTION_PATTERN.matcher(trimmed);
        if (fractionMatcher.matches()) {
            return parseSimpleFraction(fractionMatcher);
        }

        // Try whole numbers
        Matcher wholeMatcher = WHOLE_NUMBER_PATTERN.matcher(trimmed);
        if (wholeMatcher.matches()) {
            return parseWholeNumber(wholeMatcher);
        }

        throw new IllegalArgumentException("Invalid fraction format: '" + display + "'");
    }

    private Fraction parseMixedNumber(Matcher matcher) {
        try {
            long wholePart = Long.parseLong(matcher.group(1));
            long numerator = Long.parseLong(matcher.group(2));
            long denominator = Long.parseLong(matcher.group(3));

            if (denominator == 0) {
                throw new IllegalArgumentException("Denominator cannot be zero");
            }

            // Convert mixed number to improper fraction
            // For positive: whole + num/denom = (whole * denom + num) / denom
            // For negative: -whole - num/denom = -(whole * denom + num) / denom
            long improperNumerator;
            if (wholePart >= 0) {
                improperNumerator = wholePart * denominator + numerator;
            } else {
                improperNumerator = wholePart * denominator - numerator;
            }

            return new Fraction(improperNumerator, denominator);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format in mixed number");
        }
    }

    private Fraction parseSimpleFraction(Matcher matcher) {
        try {
            long numerator = Long.parseLong(matcher.group(1));
            long denominator = Long.parseLong(matcher.group(2));

            return new Fraction(numerator, denominator);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format in fraction");
        }
    }

    private Fraction parseWholeNumber(Matcher matcher) {
        try {
            long wholeNumber = Long.parseLong(matcher.group(1));
            return new Fraction(wholeNumber);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid whole number format");
        }
    }

    /**
     * Format a Fraction back to display string
     *
     * Rules:
     * - Whole numbers: "3"
     * - Proper fractions: "3/4"
     * - Improper fractions: "5/3" (could be "1 2/3" in future)
     */
    public String format(Fraction fraction) {
        if (fraction == null) {
            throw new IllegalArgumentException("Fraction cannot be null");
        }

        return fraction.toString();
    }

    /**
     * Check if a display string is a valid fraction format
     */
    public boolean isValidFormat(String display) {
        if (display == null || display.trim().isEmpty()) {
            return false;
        }

        String trimmed = display.trim();
        return MIXED_NUMBER_PATTERN.matcher(trimmed).matches() ||
                SIMPLE_FRACTION_PATTERN.matcher(trimmed).matches() ||
                WHOLE_NUMBER_PATTERN.matcher(trimmed).matches();
    }
}