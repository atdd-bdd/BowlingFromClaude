// DollarTest.java - Fixed version
import org.example.dollar.*;
// DollarTest.java - Fixed version
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;

public class DollarTest {

    @Nested
    @DisplayName("Domain Term Dollar Validation Tests")
    class DollarValidationTests {

        @Test
        @DisplayName("Valid dollar inputs should pass validation")
        void testValidInputs() {
            assertValidInput("1", true, "");
            assertValidInput("$1", true, "");
            assertValidInput("1.00", true, "");
            assertValidInput("$-1", true, "");
            assertValidInput("-$1", true, "");
            assertValidInput("(1)", true, "");
            assertValidInput("($1)", true, "");
            assertValidInput("1000.01", true, "");
            assertValidInput("1,000.01", true, "");
            assertValidInput("1,234,567.89", true, "");
            assertValidInput("1234567.89", true, "");
        }

        @Test
        @DisplayName("Invalid dollar inputs should fail validation")
        void testInvalidInputs() {
            assertValidInput("1.1", false, "Must be either zero or two digits");
            assertValidInput("($1", false, "Parathensis must match");
            assertValidInput("-($1)", false, "Double negative");
            assertValidInput("(-$1)", false, "Double negative");
            assertValidInput("10,00.00", false, "Commas must be every three digits");
            assertValidInput("1,234567.89", false, "Commas must be every three digits");
            assertValidInput("A", false, "Non-numeric");
            assertValidInput("$12345A67.80", false, "Contains non-numeric");
            assertValidInput("123$456().80", false, "Characters in incorrect order");
        }

        private void assertValidInput(String input, boolean expectedValid, String expectedMessage) {
            DollarValidationResult result = Dollar.validate(input);
            assertEquals(expectedValid, result.isValid(),
                    "Input: " + input + " - Validation result mismatch");
            if (!expectedMessage.isEmpty()) {
                assertEquals(expectedMessage, result.getMessage(),
                        "Input: " + input + " - Error message mismatch");
            }
        }
    }

    @Nested
    @DisplayName("Business Rule Dollar Input Error Messages")
    class ErrorMessageTests {

        @Test
        @DisplayName("Specific error messages for format errors")
        void testSpecificErrorMessages() {
            MoneyState state = new MoneyState(Dollar.of("10.00"), 1);

            assertErrorMessage(state, "1.1", "Must be either zero or two digits");
            assertErrorMessage(state, "($1", "Parathensis must match");
            assertErrorMessage(state, "-($1)", "Double negative");
            assertErrorMessage(state, "(-$1)", "Double negative");
            assertErrorMessage(state, "10,00.00", "Commas must be every three digits");
            assertErrorMessage(state, "1,234567.89", "Commas must be every three digits");
            assertErrorMessage(state, "A", "Non-numeric");
            assertErrorMessage(state, "$12345A67.80", "Contains non-numeric");
            assertErrorMessage(state, "123$456().80", "Characters in incorrect order");
        }

        private void assertErrorMessage(MoneyState state, String input, String expectedMessage) {
            MoneyTransactionResult result = state.addMoney(input);
            assertTrue(result.hasError(), "Should have error for input: " + input);
            assertEquals(expectedMessage, result.getMessage(),
                    "Wrong error message for input: " + input);
        }
    }

    @Nested
    @DisplayName("Money Transaction Tests")
    class MoneyTransactionTests {

        @Test
        @DisplayName("Adding valid money increases total and count")
        void testAddValidMoney() {
            MoneyState initialState = new MoneyState(Dollar.of("10.00"), 1);
            MoneyTransactionResult result = initialState.addMoney("5.00");

            assertFalse(result.hasError(), "Should not have error");
            assertEquals(Dollar.of("15.00"), result.getState().getTotal());
            assertEquals(2, result.getState().getCount());
        }

        @Test
        @DisplayName("Adding invalid money does not change state and shows specific message")
        void testAddInvalidMoneySpecificMessage() {
            MoneyState initialState = new MoneyState(Dollar.of("10.00"), 1);
            MoneyTransactionResult result = initialState.addMoney("-($1)");

            assertTrue(result.hasError(), "Should have error");
            assertEquals("Double negative", result.getMessage());
            assertEquals(Dollar.of("10.00"), result.getState().getTotal());
            assertEquals(1, result.getState().getCount());
        }

        @Test
        @DisplayName("Adding invalid money shows generic message when specified")
        void testAddInvalidMoneyGenericMessage() {
            MoneyState initialState = new MoneyState(Dollar.of("10.00"), 1);

            // Test with an input that doesn't match any specific pattern
            // This should return "Invalid format" as the generic message
            MoneyTransactionResult result = initialState.addMoney("completely_invalid_input");

            assertTrue(result.hasError(), "Should have error");
            assertEquals("Invalid format", result.getMessage());
            assertEquals(Dollar.of("10.00"), result.getState().getTotal());
            assertEquals(1, result.getState().getCount());
        }
    }

    @Nested
    @DisplayName("Dollar Formatting Tests")
    class DollarFormattingTests {

        @Test
        @DisplayName("Whole number formatting with truncation")
        void testWholeNumberFormatting() {
            Dollar d1 = Dollar.of("1.99");
            assertEquals("$1", d1.format(Dollar.Style.WHOLE_NUMBER));

            Dollar d2 = Dollar.of("-1.99");
            assertEquals("-$1", d2.format(Dollar.Style.WHOLE_NUMBER_MINUS_SIGN));
            assertEquals("($1)", d2.format(Dollar.Style.WHOLE_NUMBER_PARENTHESES));
        }

        @Test
        @DisplayName("Decimal number formatting")
        void testDecimalFormatting() {
            Dollar d1 = Dollar.of("1.99");
            assertEquals("$1.99", d1.format(Dollar.Style.DECIMAL_NUMBER));

            Dollar d2 = Dollar.of("1234567.89");
            assertEquals("$1,234,567.89", d2.format(Dollar.Style.DECIMAL_NUMBER));

            Dollar d3 = Dollar.of("-1234567000.89");
            assertEquals("($1,234,567,000.89)", d3.format(Dollar.Style.DECIMAL_NUMBER_PARENTHESES));
        }
    }

    @Nested
    @DisplayName("Dollar Arithmetic Tests")
    class DollarArithmeticTests {

        @Test
        @DisplayName("Dollar addition works correctly")
        void testAddition() {
            Dollar d1 = Dollar.of("10.50");
            Dollar d2 = Dollar.of("5.25");
            Dollar result = d1.add(d2);

            assertEquals(Dollar.of("15.75"), result);
        }

        @Test
        @DisplayName("Dollar subtraction works correctly")
        void testSubtraction() {
            Dollar d1 = Dollar.of("10.50");
            Dollar d2 = Dollar.of("5.25");
            Dollar result = d1.subtract(d2);

            assertEquals(Dollar.of("5.25"), result);
        }

        @Test
        @DisplayName("Dollar creation from various inputs")
        void testDollarCreation() {
            assertDoesNotThrow(() -> Dollar.of("$10.00"));
            assertDoesNotThrow(() -> Dollar.of("(5.50)"));
            assertDoesNotThrow(() -> Dollar.of("1,234.56"));

            assertThrows(IllegalArgumentException.class, () -> Dollar.of("invalid"));
            assertThrows(IllegalArgumentException.class, () -> Dollar.of("1.1"));
        }
    }
}