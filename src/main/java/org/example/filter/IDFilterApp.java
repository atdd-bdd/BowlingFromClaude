package org.example.filter;

import java.util.*;

public class IDFilterApp {

    public static void main(String[] args) {
        System.out.println("=== ID VALIDATION AND DATA FILTERING ===");

        // Test ID validation
        testIDValidation();
        System.out.println();

        // Test data filtering
        testDataFiltering();
        System.out.println();

        // Interactive mode
        runInteractiveMode();
    }

    private static void testIDValidation() {
        System.out.println("Testing ID Validation:");
        String[] testIDs = {"Q1234", "Q123", "Q12345", "A1234"};

        for (String testID : testIDs) {
            ValueValid result = IDValidator.validateID(testID);
            System.out.printf("ID: %-6s | Valid: %-5s | Notes: %s%n",
                    result.getValue(), result.isValid(), result.getNotes());
        }
    }

    private static void testDataFiltering() {
        System.out.println("Testing Data Filtering:");

        // Create test data
        DataFilter filter = new DataFilter();
        filter.addData("Q1234", 1);
        filter.addData("Q9999", 2);
        filter.addData("Q1234", 3);

        System.out.println("Test Data:");
        for (LabelValue item : filter.getData()) {
            System.out.printf("  ID: %s, Value: %d%n", item.getId(), item.getValue());
        }

        // Test filtering
        System.out.println("\nFiltering by ID 'Q1234':");
        int sum = filter.filterByIDAndSum("Q1234");
        System.out.printf("Sum: %d%n", sum);

        // Test with FilterValue object
        System.out.println("\nUsing FilterValue object:");
        FilterValue filterValue = new FilterValue("ID", "Q1234");
        ResultValue result = filter.filterBy(filterValue);
        System.out.printf("Result Sum: %d%n", result.getSum());
    }

    private static void runInteractiveMode() {
        Scanner scanner = new Scanner(System.in);
        DataFilter filter = new DataFilter();

        System.out.println("Interactive Mode:");
        System.out.println("Commands:");
        System.out.println("  add <ID> <value> - Add data item");
        System.out.println("  filter <ID>      - Filter and sum by ID");
        System.out.println("  validate <ID>    - Validate an ID");
        System.out.println("  list             - Show all data");
        System.out.println("  quit             - Exit");
        System.out.println();

        while (true) {
            System.out.print("Enter command: ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("quit")) {
                break;
            }

            String[] parts = input.split("\\s+");
            if (parts.length == 0) continue;

            String command = parts[0].toLowerCase();

            try {
                switch (command) {
                    case "add":
                        if (parts.length == 3) {
                            String id = parts[1];
                            int value = Integer.parseInt(parts[2]);

                            // Validate ID first
                            ValueValid validation = IDValidator.validateID(id);
                            if (validation.isValid()) {
                                filter.addData(id, value);
                                System.out.printf(" Added: ID=%s, Value=%d%n", id, value);
                            } else {
                                System.out.printf("X Invalid ID '%s': %s%n", id, validation.getNotes());
                            }
                        } else {
                            System.out.println("Usage: add <ID> <value>");
                        }
                        break;

                    case "filter":
                        if (parts.length == 2) {
                            String id = parts[1];
                            ValueValid validation = IDValidator.validateID(id);
                            if (validation.isValid()) {
                                int sum = filter.filterByIDAndSum(id);
                                List<LabelValue> filtered = filter.getFilteredData(id);
                                System.out.printf("Filtered by ID '%s':%n", id);
                                for (LabelValue item : filtered) {
                                    System.out.printf("  Value: %d%n", item.getValue());
                                }
                                System.out.printf("Sum: %d%n", sum);
                            } else {
                                System.out.printf("X Invalid ID '%s': %s%n", id, validation.getNotes());
                            }
                        } else {
                            System.out.println("Usage: filter <ID>");
                        }
                        break;

                    case "validate":
                        if (parts.length == 2) {
                            String id = parts[1];
                            ValueValid validation = IDValidator.validateID(id);
                            System.out.printf("ID '%s': Valid=%s, Notes=%s%n",
                                    id, validation.isValid(), validation.getNotes());
                        } else {
                            System.out.println("Usage: validate <ID>");
                        }
                        break;

                    case "list":
                        List<LabelValue> data = filter.getData();
                        if (data.isEmpty()) {
                            System.out.println("No data available.");
                        } else {
                            System.out.println("All Data:");
                            for (LabelValue item : data) {
                                System.out.printf("  ID: %s, Value: %d%n", item.getId(), item.getValue());
                            }
                        }
                        break;

                    default:
                        System.out.println("Unknown command. Type 'quit' to exit.");
                }
            } catch (NumberFormatException e) {
                System.out.println("X Invalid number format.");
            } catch (Exception e) {
                System.out.println("X Error: " + e.getMessage());
            }

            System.out.println();
        }

        scanner.close();
        System.out.println("Thanks for using the ID Filter program!");
    }
}