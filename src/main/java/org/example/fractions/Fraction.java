package org.example.fractions;

import java.math.BigInteger;
import java.util.Objects;

/**
 * Immutable value object representing a mathematical fraction
 */
public class Fraction {
    private final BigInteger numerator;
    private final BigInteger denominator;

    public Fraction(BigInteger numerator, BigInteger denominator) {
        if (denominator == null || denominator.equals(BigInteger.ZERO)) {
            throw new IllegalArgumentException("Denominator cannot be zero or null");
        }
        if (numerator == null) {
            throw new IllegalArgumentException("Numerator cannot be null");
        }

        // Normalize negative fractions (denominator should be positive)
        if (denominator.compareTo(BigInteger.ZERO) < 0) {
            this.numerator = numerator.negate();
            this.denominator = denominator.negate();
        } else {
            this.numerator = numerator;
            this.denominator = denominator;
        }
    }

    // Convenience constructors
    public Fraction(long numerator, long denominator) {
        this(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
    }

    public Fraction(int numerator, int denominator) {
        this(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
    }

    // Whole number constructor
    public Fraction(long wholeNumber) {
        this(BigInteger.valueOf(wholeNumber), BigInteger.ONE);
    }

    public Fraction(int wholeNumber) {
        this(BigInteger.valueOf(wholeNumber), BigInteger.ONE);
    }

    public BigInteger getNumerator() {
        return numerator;
    }

    public BigInteger getDenominator() {
        return denominator;
    }

    public Fraction add(Fraction other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot add null fraction");
        }

        // a/b + c/d = (a*d + b*c) / (b*d)
        BigInteger newNumerator = this.numerator.multiply(other.denominator)
                .add(this.denominator.multiply(other.numerator));
        BigInteger newDenominator = this.denominator.multiply(other.denominator);

        return new Fraction(newNumerator, newDenominator).reduce();
    }

    public Fraction multiply(Fraction other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot multiply by null fraction");
        }

        BigInteger newNumerator = this.numerator.multiply(other.numerator);
        BigInteger newDenominator = this.denominator.multiply(other.denominator);

        return new Fraction(newNumerator, newDenominator).reduce();
    }

    public Fraction reduce() {
        BigInteger gcd = numerator.gcd(denominator);

        if (gcd.equals(BigInteger.ONE)) {
            return this; // Already reduced
        }

        return new Fraction(
                numerator.divide(gcd),
                denominator.divide(gcd)
        );
    }

    public boolean isWholeNumber() {
        return denominator.equals(BigInteger.ONE);
    }

    public boolean isProperFraction() {
        return numerator.abs().compareTo(denominator) < 0;
    }

    // Factory methods
    public static Fraction of(long numerator, long denominator) {
        return new Fraction(numerator, denominator);
    }

    public static Fraction of(int numerator, int denominator) {
        return new Fraction(numerator, denominator);
    }

    public static Fraction wholeNumber(long number) {
        return new Fraction(number);
    }

    public static Fraction zero() {
        return new Fraction(0, 1);
    }

    public static Fraction one() {
        return new Fraction(1, 1);
    }

    @Override
    public String toString() {
        Fraction reduced = this.reduce();
        if (reduced.isWholeNumber()) {
            return reduced.numerator.toString();
        } else {
            return reduced.numerator + "/" + reduced.denominator;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Fraction fraction = (Fraction) obj;

        // Compare reduced forms for equality
        Fraction thisReduced = this.reduce();
        Fraction otherReduced = fraction.reduce();

        return Objects.equals(thisReduced.numerator, otherReduced.numerator) &&
                Objects.equals(thisReduced.denominator, otherReduced.denominator);
    }

    @Override
    public int hashCode() {
        Fraction reduced = this.reduce();
        return Objects.hash(reduced.numerator, reduced.denominator);
    }
}