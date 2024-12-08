package utils;

import java.util.*;
import java.util.stream.Collectors;

public class DataCleaningUtils {
    public static Object[][] cleanData(Object[][] data, String placeholder) {
        Object[][] cleanedData = new Object[data.length][];
        for (int i = 0; i < data.length; i++) {
            cleanedData[i] = cleanRow(data[i], placeholder);
        }
        return cleanedData;
    }

    private static Object[] cleanRow(Object[] row, String placeholder) {
        List<Object> filledRow = fillMissing(row, placeholder);
        filledRow = cleanRow(filledRow);
        filledRow = removeDuplicate(filledRow);
        return filledRow.toArray();
    }

    private static List<Object> cleanRow(List<Object> row) {
        return row.stream()
                .filter(DataCleaningUtils::isValid)
                .map(value -> value != null ? value.toString().trim() : "")
                .collect(Collectors.toList());
    }

    private static boolean isValid(Object value) {
        return value != null && !value.toString().trim().isEmpty();
    }

    private static List<Object> removeDuplicate(List<Object> row) {
        return new ArrayList<>(new LinkedHashSet<>(row));
    }

    private static List<Object> fillMissing(Object[] row, String placeholder) {
        return Arrays.stream(row)
                .map(value -> (value == null || value.toString().trim().isEmpty()) ? placeholder : value)
                .collect(Collectors.toList());
    }
/*
    // Handle numeric placeholders (e.g., default value for numeric fields)
    private static Object handleNumericPlaceholder(Object value) {
        try {
            // Try parsing the value as a numeric type (e.g., double)
            return NumberFormat.getInstance().parse(value.toString()).doubleValue();
        } catch (Exception e) {
            // Return a default numeric value (like 0.0) for invalid data
            return 0.0;
        }
    }

    // Check if a value can be parsed as numeric
    private static boolean isNumeric(String value) {
        try {
            Double.parseDouble(value.replaceAll("[^0-9.-]", ""));  // Remove currency symbols and commas
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Fill missing values with placeholder or default values for numeric types
    private static List<Object> fillMissing(Object[] row, String placeholder, boolean handleNumericMissing) {
        List<Object> filledRow = Arrays.stream(row)
                .map(value -> (value == null || value.toString().trim().isEmpty())
                        ? (handleNumericMissing && isNumeric(value.toString()) ? handleNumericPlaceholder(value) : placeholder)
                        : value)
                .collect(Collectors.toList());

        return filledRow;
    }

    // Log cleaning issues (e.g., missing or invalid data)
    private static void logCleaningIssues(Object value) {
        if (value == null || value.toString().trim().isEmpty()) {
            System.out.println("Warning: Missing data encountered.");
        }
    }

    // Example: Customize handling based on feature types
    public static void applyFeatureSpecificCleanings(Object[][] data, String featureName) {
        // Example: Apply different cleaning strategies based on feature name (fuelType, price, etc.)
        if ("price".equals(featureName)) {
            // Clean price fields (e.g., removing currency symbols, etc.)
            // Custom processing logic for 'price'
        }
    }
*/
}
