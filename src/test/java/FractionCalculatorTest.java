import org.example.fractions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests validating Gherkin scenarios for the Fraction Calculator
 */
public class FractionCalculatorTest {

    private FractionCalculator calculator;
    private FractionParser parser;

    @BeforeEach
    public void setUp() {
        parser = new FractionParser();
        calculator = new FractionCalculator(parser);
    }

    @Test
    public void testFractionCalculatorScenario() {
        // Scenario: Fraction Calculator
        // Given fraction calculator is active
        // When user enters "3/4 + 5/8 + 3"
        // Then the total is shown as "35/8"

        Fraction result = calculator.calculate("3/4 + 5/8 + 3");
        Fraction expected = new Fraction(35, 8);

        assertEquals(expected, result);
        assertEquals("35/8", result.toString());
    }

    @Test
    public void testFractionCalculationExternalBehavior() {
        // Scenario: Fraction Calculation External Behavior
        // Expression: 3/4 + 5/8 + 3, Result: 35/8

        Fraction result = calculator.calculate("3/4 + 5/8 + 3");
        assertEquals("35/8", result.toString());
    }

    @Test
    public void testExpressionsToCalculate() {
        // Scenario: Expressions to Calculate
        // | Expression | Result |
        // | 3/4 + 5/8  | 11/8   |
        // | 11/8 + 3   | 35/8   |

        assertEquals("11/8", calculator.calculate("3/4 + 5/8").toString());
        assertEquals("35/8", calculator.calculate("11/8 + 3").toString());
    }

    @Test
    public void testBusinessRuleDisplayToFraction() {
        // Scenario: Business Rule Display to Fraction
        // | Display | Numerator | Denominator | Notes                   |
        // | 3/4     | 3         | 4           | Part of expression test |
        // | 5/8     | 5         | 8           | Part of expression test |
        // | 3       | 3         | 1           | Whole number            |

        Fraction fraction1 = parser.parse("3/4");
        assertEquals(3, fraction1.getNumerator().intValue());
        assertEquals(4, fraction1.getDenominator().intValue());

        Fraction fraction2 = parser.parse("5/8");
        assertEquals(5, fraction2.getNumerator().intValue());
        assertEquals(8, fraction2.getDenominator().intValue());

        Fraction fraction3 = parser.parse("3");
        assertEquals(3, fraction3.getNumerator().intValue());
        assertEquals(1, fraction3.getDenominator().intValue());
    }

    @Test
    public void testBusinessRuleFractionToDisplay() {
        // Scenario: Business Rule Fraction to Display
        // | Numerator | Denominator | Display | Notes                   |
        // | 35        | 8           | 35/8    | Part of expression test |

        Fraction fraction = new Fraction(35, 8);
        assertEquals("35/8", fraction.toString());
    }

    @Test
    public void testDisplayToComponents() {
        // Scenario: Display to Components
        // | Expression | In1 | Op | In2 |
        // | 3/4 + 5/8  | 3/4 | +  | 5/8 |

        FractionCalculator.ExpressionComponents components =
                calculator.parseComponents("3/4 + 5/8");

        assertEquals(new Fraction(3, 4), components.getLeftOperand());
        assertEquals(Operator.ADD, components.getOperator());
        assertEquals(new Fraction(5, 8), components.getRightOperand());
    }

    @Test
    public void testBusinessRuleOperations() {
        // Scenario: Business Rule Operations
        // | In1  | Op | In2 | Result | Notes                   |
        // | 3/4  | +  | 5/8 | 11/8   | Part of Expression Test |
        // | 11/8 | +  | 3   | 35/8   | Part of Expression Test |

        Fraction result1 = Operator.ADD.apply(new Fraction(3, 4), new Fraction(5, 8));
        assertEquals(new Fraction(11, 8), result1);

        Fraction result2 = Operator.ADD.apply(new Fraction(11, 8), new Fraction(3));
        assertEquals(new Fraction(35, 8), result2);
    }

    @Test
    public void testDomainTermOperators() {
        // Scenario: Domain Term Operators
        // | Symbol | Meaning        | Notes                |
        // | +      | Addition       |                      |
        // | *      | Multiplication | To do next iteration |

        assertEquals("+", Operator.ADD.getSymbol());
        assertEquals("Addition", Operator.ADD.getDescription());

        assertEquals("*", Operator.MULTIPLY.getSymbol());
        assertEquals("Multiplication", Operator.MULTIPLY.getDescription());

        assertEquals(Operator.ADD, Operator.fromSymbol("+"));
        assertEquals(Operator.MULTIPLY, Operator.fromSymbol("*"));
    }

    @Test
    public void testBusinessRuleDisplayToFractionExtended() {
        // Scenario: Business Rule Display to Fraction (Extended)
        // | Display | Numerator | Denominator | Notes              |
        // | -3      | -3        | 1           | Negative number    |
        // | 1/3     | 1         | 3           | Fraction           |
        // | 2/4     | 1         | 2           | Fraction reduced   |
        // | 1 1/3   | 4         | 3           | Whole and fraction |

        Fraction negativeThree = parser.parse("-3");
        assertEquals(-3, negativeThree.getNumerator().intValue());
        assertEquals(1, negativeThree.getDenominator().intValue());

        Fraction oneThird = parser.parse("1/3");
        assertEquals(1, oneThird.getNumerator().intValue());
        assertEquals(3, oneThird.getDenominator().intValue());

        Fraction twoFourths = parser.parse("2/4");
        Fraction reduced = twoFourths.reduce();
        assertEquals(1, reduced.getNumerator().intValue());
        assertEquals(2, reduced.getDenominator().intValue());

        Fraction mixedNumber = parser.parse("1 1/3");
        assertEquals(4, mixedNumber.getNumerator().intValue());
        assertEquals(3, mixedNumber.getDenominator().intValue());
    }

    @Test
    public void testBusinessRuleFractionToDisplayExtended() {
        // Scenario: Business Rule Fraction to Display (Extended)
        // | Numerator | Denominator | Display | Notes                |
        // | 3         | 1           | 3       | Whole Number         |
        // | -3        | 1           | -3      | Negative Numerator   |
        // | 1         | 3           | 1/3     | Fraction             |
        // | 3         | 3           | 1       | Whole Number         |

        assertEquals("3", new Fraction(3, 1).toString());
        assertEquals("-3", new Fraction(-3, 1).toString());
        assertEquals("1/3", new Fraction(1, 3).toString());
        assertEquals("1", new Fraction(3, 3).toString());
    }

    @Test
    public void testBusinessRuleOperationsExtended() {
        // Scenario: Business Rule Operations (Extended)
        // | In1   | Op | In2   | Result | Notes                           |
        // | 3     | +  | 2     | 5      | Whole Number                    |
        // | 1/3   | +  | 1/3   | 2/3    | Two fractions, same denominator |
        // | 1/3   | +  | 2/3   | 1      | Whole number result             |
        // | 1/3   | +  | 1/2   | 5/6    | Different denominators          |
        // | 1/6   | +  | 1/8   | 7/24   | Fraction and reduce             |
        // | 1/4   | +  | 1/4   | 1/2    | Fraction and reduce             |

        assertEquals("5", calculator.calculate("3 + 2").toString());
        assertEquals("2/3", calculator.calculate("1/3 + 1/3").toString());
        assertEquals("1", calculator.calculate("1/3 + 2/3").toString());
        assertEquals("5/6", calculator.calculate("1/3 + 1/2").toString());
        assertEquals("7/24", calculator.calculate("1/6 + 1/8").toString());
        assertEquals("1/2", calculator.calculate("1/4 + 1/4").toString());
    }

    @Test
    public void testFractionReduction() {
        // Test fraction reduction functionality
        Fraction unreduced = new Fraction(4, 8);
        Fraction reduced = unreduced.reduce();

        assertEquals(new Fraction(1, 2), reduced);
        assertEquals("1/2", reduced.toString());
    }

    @Test
    public void testNegativeFractions() {
        // Test negative fraction handling
        Fraction negative1 = new Fraction(-3, 4);
        assertEquals("-3/4", negative1.toString());

        Fraction negative2 = new Fraction(3, -4);
        assertEquals("-3/4", negative2.toString()); // Should normalize to negative numerator

        Fraction result = calculator.calculate("-3/4 + 1/4");
        assertEquals("-1/2", result.toString());
    }

    @Test
    public void testMixedNumberParsing() {
        // Test mixed number parsing like "1 1/3"
        Fraction mixed = parser.parse("1 1/3");
        assertEquals(new Fraction(4, 3), mixed);

        Fraction negativeMixed = parser.parse("-2 3/4");
        assertEquals(new Fraction(-11, 4), negativeMixed);
    }

    @Test
    public void testZeroAndOne() {
        // Test special cases
        assertEquals("0", Fraction.zero().toString());
        assertEquals("1", Fraction.one().toString());

        Fraction zeroResult = calculator.calculate("1/3 + -1/3");
        assertEquals(Fraction.zero(), zeroResult);
    }

    @Test
    public void testErrorHandling() {
        // Test various error conditions
        assertThrows(IllegalArgumentException.class, () -> new Fraction(1, 0));
        assertThrows(IllegalArgumentException.class, () -> parser.parse(""));
        assertThrows(IllegalArgumentException.class, () -> parser.parse("invalid"));
        assertThrows(IllegalArgumentException.class, () -> calculator.calculate(""));
        assertThrows(IllegalArgumentException.class, () -> Operator.fromSymbol("?"));
    }

    @Test
    public void testMultiplication() {
        // Test multiplication operator (for future iteration)
        Fraction result = Operator.MULTIPLY.apply(new Fraction(3, 4), new Fraction(2, 3));
        assertEquals(new Fraction(1, 2), result);
        assertEquals("1/2", result.toString());
    }
}