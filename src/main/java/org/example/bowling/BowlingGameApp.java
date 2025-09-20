package org.example.bowling;

// Interactive Bowling Game Application
import java.util.Scanner;
import java.util.List;
public class BowlingGameApp {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== BOWLING GAME ===");
        System.out.println("Enter rolls one at a time. Enter 0-10 for number of pins knocked down.");
        System.out.println("The game will automatically handle scoring and end when complete.");
        System.out.println("========================================\n");

        BowlingGame game = new BowlingGame();

        while (!game.isGameComplete()) {
            // Score the current state
            game.score();

            // Display current game state
            displayGameState(game);

            // Get next roll from user
            Roll nextRoll = getNextRoll(game);

            if (nextRoll != null) {
                // Try to add the roll
                int rollsBefore = game.getRolls().size();
                game.addRoll(nextRoll);
                int rollsAfter = game.getRolls().size();

                if (rollsAfter == rollsBefore) {
                    System.out.println("X Invalid roll! That would exceed 10 pins for this frame.");
                    System.out.println();
                } else {
                    System.out.println(" Roll accepted!");
                    System.out.println();
                }
            }
        }

        // Final scoring and display
        game.score();
        System.out.println("🎉 GAME COMPLETE! 🎉");
        System.out.println("Final Score:");
        displayGameState(game);

        // Display final total
        List<FrameValues> frameValues = game.getFrameValues();
        int finalScore = frameValues.get(9).getTotalScore();
        System.out.printf("Your total score: %d\n", finalScore);

        scanner.close();
    }

    private static void displayGameState(BowlingGame game) {
        System.out.println("Current Game:");
        String display = game.getDisplay();
        System.out.println(display);

        InputControlValues ic = game.getInputControl();
        if (!game.isGameComplete()) {
            System.out.printf("Next: Frame %d, Roll %d (Max pins: %d)\n",
                    ic.getFrame(), ic.getRoll(), ic.getRemaining());
        }
        System.out.println();
    }

    private static Roll getNextRoll(BowlingGame game) {
        InputControlValues ic = game.getInputControl();

        while (true) {
            System.out.printf("Enter roll for Frame %d, Roll %d (0-%d pins): ",
                    ic.getFrame(), ic.getRoll(), ic.getRemaining());

            String input = scanner.nextLine().trim();

            // Check for exit command
            if (input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("exit")) {
                System.out.println("Thanks for playing!");
                System.exit(0);
            }

            // Try to parse as integer
            try {
                int pins = Integer.parseInt(input);
                Roll roll = new Roll(pins);

                if (!roll.isValid()) {
                    System.out.println("X Invalid input! Please enter a number between 0 and 10.");
                    continue;
                }

                // Additional validation for remaining pins
                if (pins > ic.getRemaining()) {
                    System.out.printf("X Too many pins! Maximum for this roll is %d.\n", ic.getRemaining());
                    continue;
                }

                return roll;

            } catch (NumberFormatException e) {
                System.out.println("X Invalid input! Please enter a number between 0 and 10, or 'quit' to exit.");
            }
        }
    }
}

// Test class to verify the scenarios (kept for reference)
