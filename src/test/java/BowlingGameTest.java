// JUnit 5 Test class to verify the scenarios
import org.example.bowling.BowlingGame;
import org.example.bowling.FrameValues;
import org.example.bowling.InputControlValues;
import org.example.bowling.Roll;
import org.junit.jupiter.api.Test;
        import org.junit.jupiter.api.BeforeEach;
        import org.junit.jupiter.api.DisplayName;
        import org.junit.jupiter.api.Nested;
        import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
public class BowlingGameTest {

    private BowlingGame game;

    @BeforeEach
    void setUp() {
        game = new BowlingGame();
    }

    @Nested
    @DisplayName("Roll Validation Tests")
    class RollValidationTests {

        @Test
        @DisplayName("Valid rolls should be accepted")
        void testValidRolls() {
            Roll validRoll0 = new Roll(0);
            Roll validRoll10 = new Roll(10);
            Roll validRollTBR = new Roll(Roll.TBR);

            assertTrue(validRoll0.isValid(), "Roll with value 0 should be valid");
            assertTrue(validRoll10.isValid(), "Roll with value 10 should be valid");
            assertTrue(validRollTBR.isValid(), "Roll with TBR should be valid");
        }

        @Test
        @DisplayName("Invalid rolls should be rejected")
        void testInvalidRolls() {
            Roll invalidRoll11 = new Roll(11);
            Roll invalidRollNegative = new Roll(-2);

            assertFalse(invalidRoll11.isValid(), "Roll with value 11 should be invalid");
            assertFalse(invalidRollNegative.isValid(), "Roll with value -2 should be invalid");
        }
    }

    @Test
    @DisplayName("Adding a roll scenario")
    void testAddingRoll() {
        // Given rolls are
        int[] givenRolls = {5, 5, 4, 5, 8, 2, 10, 0, 10, 10, 6, 2, 10, 4, 6, 10};
        for (int roll : givenRolls) {
            game.addRoll(new Roll(roll));
        }

        // When roll is 10
        game.addRoll(new Roll(10));

        // Then rolls become (should include the new 10)
        assertEquals(17, game.getRolls().size(), "Should have 17 rolls after adding the 10");
    }

    @Test
    @DisplayName("Full Game Compute and Display")
    void testFullGameComputeAndDisplay() {
        // Given rolls
        int[] rolls = {5, 5, 4, 5, 8, 2, 10, 0, 10, 10, 6, 2, 10, 4, 6, 10, 10};
        for (int roll : rolls) {
            game.addRoll(new Roll(roll));
        }

        // When scored
        game.score();

        // Then display should contain proper bowling notation
        String display = game.getDisplay();
        assertNotNull(display, "Display should not be null");
        assertTrue(display.contains("5/"), "Display should contain spare notation");
        assertTrue(display.contains("X"), "Display should contain strike notation");
        assertTrue(display.contains("149"), "Display should contain score 149");
    }

    @Test
    @DisplayName("A Game in Steps - Frame Values Verification")
    void testGameInSteps() {
        // Given rolls
        int[] rolls = {5, 5, 4, 5, 8, 2, 10, 0, 10, 10, 6, 2, 10, 4, 6, 10, 10};
        for (int roll : rolls) {
            game.addRoll(new Roll(roll));
        }

        // When scored
        game.score();

        // Then frame values should match expected results
        List<FrameValues> frameValues = game.getFrameValues();

        // Frame 1: 5, 5 (spare) - score 14, total 14
        FrameValues frame1 = frameValues.get(0);
        assertEquals(5, frame1.getRoll1().getIntValue(), "Frame 1 roll 1 should be 5");
        assertEquals(5, frame1.getRoll2().getIntValue(), "Frame 1 roll 2 should be 5");
        assertEquals(14, frame1.getScore(), "Frame 1 score should be 14");
        assertEquals(14, frame1.getTotalScore(), "Frame 1 total should be 14");

        // Frame 2: 4, 5 (open) - score 9, total 23  
        FrameValues frame2 = frameValues.get(1);
        assertEquals(4, frame2.getRoll1().getIntValue(), "Frame 2 roll 1 should be 4");
        assertEquals(5, frame2.getRoll2().getIntValue(), "Frame 2 roll 2 should be 5");
        assertEquals(9, frame2.getScore(), "Frame 2 score should be 9");
        assertEquals(23, frame2.getTotalScore(), "Frame 2 total should be 23");

        // Frame 4: Strike (10) - score 20, total 63
        FrameValues frame4 = frameValues.get(3);
        assertEquals(10, frame4.getRoll1().getIntValue(), "Frame 4 roll 1 should be 10 (strike)");
        assertEquals(20, frame4.getScore(), "Frame 4 score should be 20");
        assertEquals(63, frame4.getTotalScore(), "Frame 4 total should be 63");

        // Verify total progresses correctly
        assertEquals(149, frameValues.get(8).getTotalScore(), "Frame 9 total should be 149");
    }

    @Test
    @DisplayName("Check for Game Complete - Complete Game")
    void testGameComplete() {
        // Given rolls including final roll
        int[] rolls = {5, 5, 4, 5, 8, 2, 10, 0, 10, 10, 6, 2, 10, 4, 6, 10, 10, 10};
        for (int roll : rolls) {
            game.addRoll(new Roll(roll));
        }

        // When scored
        game.score();

        // Then game should be complete
        assertTrue(game.isGameComplete(), "Game should be complete after all required rolls");
    }

    @Test
    @DisplayName("Check for Game Complete - Incomplete Game")
    void testGameIncomplete() {
        // Given rolls without final roll
        int[] rolls = {5, 5, 4, 5, 8, 2, 10, 0, 10, 10, 6, 2, 10, 4, 6, 10, 10};
        for (int roll : rolls) {
            game.addRoll(new Roll(roll));
        }

        // When scored
        game.score();

        // Then game should not be complete
        assertFalse(game.isGameComplete(), "Game should not be complete - missing final roll");
    }

    @Test
    @DisplayName("Input Control Should Be For Next Frame After Strike")
    void testInputControlAfterStrike() {
        // Given a strike on first roll
        game.addRoll(new Roll(10));

        // When scored
        game.score();

        // Then input control should be for frame 2, roll 1
        InputControlValues ic = game.getInputControl();
        assertEquals(2, ic.getFrame(), "Should be frame 2 after strike");
        assertEquals(1, ic.getRoll(), "Should be roll 1 of next frame");
        assertEquals(10, ic.getRemaining(), "Should have 10 pins remaining");
    }

    @Test
    @DisplayName("Try to add invalid roll - exceeds frame total")
    void testInvalidRollExceedsFrameTotal() {
        // Given first roll of 5
        game.addRoll(new Roll(5));
        game.score();
        int rollsAfterFirst = game.getRolls().size();

        // When trying to add roll of 6 (5 + 6 > 10)
        game.addRoll(new Roll(6));
        game.score();
        int rollsAfterSecond = game.getRolls().size();

        // Then rolls count should remain the same (invalid roll rejected)
        assertEquals(1, rollsAfterFirst, "Should have 1 roll after adding 5");
        assertEquals(1, rollsAfterSecond, "Should still have 1 roll after trying to add invalid 6");
    }

    @Nested
    @DisplayName("Tenth Frame Tests")
    class TenthFrameTests {

        @Test
        @DisplayName("Values for Tenth Frame with two strikes")
        void testTenthFrameValues() {
            // Set up game to 10th frame
            int[] rollsToTenth = {5, 5, 4, 5, 8, 2, 10, 0, 10, 10, 6, 2, 10, 4, 6};
            for (int roll : rollsToTenth) {
                game.addRoll(new Roll(roll));
            }

            // Given rolls for tenth frame are 10, 10
            game.addRoll(new Roll(10));
            game.addRoll(new Roll(10));

            // When scored
            game.score();

            // Then tenth frame values should be correct
            FrameValues tenthFrame = game.getFrameValues().get(9);
            assertEquals(10, tenthFrame.getFrame(), "Should be frame 10");
            assertEquals(10, tenthFrame.getRoll1().getIntValue(), "Roll 1 should be 10");
            assertEquals(10, tenthFrame.getRoll2().getIntValue(), "Roll 2 should be 10");
            assertEquals(Roll.TBR, tenthFrame.getRoll3().getValue(), "Roll 3 should be TBR");
            assertEquals(FrameValues.TBS, tenthFrame.getScore(), "Score should be TBS (not complete)");
        }
    }

    @Nested
    @DisplayName("Display Format Tests")
    class DisplayFormatTests {

        @Test
        @DisplayName("Strike should display as X")
        void testStrikeDisplay() {
            game.addRoll(new Roll(10));
            game.score();

            String display = game.getDisplay();
            assertTrue(display.contains("X"), "Strike should be displayed as X");
        }

        @Test
        @DisplayName("Spare should display with / notation")
        void testSpareDisplay() {
            game.addRoll(new Roll(7));
            game.addRoll(new Roll(3));
            game.score();

            String display = game.getDisplay();
            assertTrue(display.contains("7/"), "Spare should be displayed with / notation");
        }

        @Test
        @DisplayName("Gutter ball should display as -")
        void testGutterDisplay() {
            game.addRoll(new Roll(0));
            game.addRoll(new Roll(5));
            game.score();

            String display = game.getDisplay();
            assertTrue(display.contains("-"), "Gutter ball should be displayed as -");
        }
    }
}
