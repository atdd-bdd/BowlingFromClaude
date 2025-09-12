// JUnit 5 Tests
import org.example.filter.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class IDFilterTest {

    @Nested
    @DisplayName("Domain Term ID Tests")
    class IDValidationTests {

        @Test
        @DisplayName("ID must have exactly 5 letters and begin with Q")
        void testIDValidation() {
            // Test cases from the specification
            ValueValid result1 = IDValidator.validateID("Q1234");
            assertTrue(result1.isValid(), "Q1234 should be valid");
            assertEquals("", result1.getNotes());

            ValueValid result2 = IDValidator.validateID("Q123");
            assertFalse(result2.isValid(), "Q123 should be invalid (too short)");
            assertEquals("Too short", result2.getNotes());

            ValueValid result3 = IDValidator.validateID("Q12345");
            assertFalse(result3.isValid(), "Q12345 should be invalid (too long)");
            assertEquals("Too long", result3.getNotes());

            ValueValid result4 = IDValidator.validateID("A1234");
            assertFalse(result4.isValid(), "A1234 should be invalid (must begin with Q)");
            assertEquals("Must begin with Q", result4.getNotes());
        }

        @Test
        @DisplayName("ID class should validate correctly")
        void testIDClass() {
            ID validId = new ID("Q1234");
            assertTrue(validId.isValid());

            ID invalidId = new ID("Q123");
            assertFalse(invalidId.isValid());
        }
    }

    @Nested
    @DisplayName("Filter Data Tests")
    class FilterDataTests {

        private DataFilter dataFilter;

        @BeforeEach
        void setUp() {
            dataFilter = new DataFilter();
            // Given list of numbers
            dataFilter.addData("Q1234", 1);
            dataFilter.addData("Q9999", 2);
            dataFilter.addData("Q1234", 3);
        }

        @Test
        @DisplayName("Filter data by ID with value")
        void testFilterByIDWithValue() {
            // When filtered by ID with value Q1234
            int sum = dataFilter.filterByIDAndSum("Q1234");

            // Then sum is 4 (1 + 3)
            assertEquals(4, sum);
        }

        @Test
        @DisplayName("Filter data another way using FilterValue")
        void testFilterDataAnotherWay() {
            // When filtered by FilterValue with Name="ID", Value="Q1234"
            FilterValue filter = new FilterValue("ID", "Q1234");
            ResultValue result = dataFilter.filterBy(filter);

            // Then result sum is 4
            assertEquals(4, result.getSum());
        }

        @Test
        @DisplayName("Filter should return correct items")
        void testFilteredItems() {
            List<LabelValue> filtered = dataFilter.getFilteredData("Q1234");
            assertEquals(2, filtered.size());
            assertEquals(1, filtered.get(0).getValue());
            assertEquals(3, filtered.get(1).getValue());
        }

        @Test
        @DisplayName("Filter by non-existent ID should return 0")
        void testFilterNonExistentID() {
            int sum = dataFilter.filterByIDAndSum("Q0000");
            assertEquals(0, sum);
        }
    }

    @Nested
    @DisplayName("Data Class Tests")
    class DataClassTests {

        @Test
        @DisplayName("LabelValue should work correctly")
        void testLabelValue() {
            LabelValue item = new LabelValue("Q1234", 42);
            assertEquals("Q1234", item.getId().getValue());
            assertEquals(42, item.getValue());
        }

        @Test
        @DisplayName("FilterValue should work correctly")
        void testFilterValue() {
            FilterValue filter = new FilterValue("ID", "Q1234");
            assertEquals("ID", filter.getName());
            assertEquals("Q1234", filter.getValue().getValue());
        }

        @Test
        @DisplayName("ResultValue should work correctly")
        void testResultValue() {
            ResultValue result = new ResultValue(100);
            assertEquals(100, result.getSum());
        }

        @Test
        @DisplayName("Default constructors should work")
        void testDefaultConstructors() {
            ValueValid vv = new ValueValid();
            assertEquals("0", vv.getValue());
            assertFalse(vv.isValid());
            assertEquals("", vv.getNotes());

            LabelValue lv = new LabelValue();
            assertEquals("Q0000", lv.getId().getValue());
            assertEquals(0, lv.getValue());

            FilterValue fv = new FilterValue();
            assertEquals("", fv.getName());
            assertEquals("Q0000", fv.getValue().getValue());

            ResultValue rv = new ResultValue();
            assertEquals(0, rv.getSum());
        }
    }
}