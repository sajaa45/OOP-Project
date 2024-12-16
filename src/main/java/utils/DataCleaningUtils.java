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
        return row.stream().filter(value -> isValid(value) && !isNumeric(value.toString())).map(value -> value.toString().trim()).collect(Collectors.toList());
    }

    private static boolean isNumeric(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isValid(Object value) {
        return value != null && !value.toString().trim().isEmpty();
    }

    private static List<Object> removeDuplicate(List<Object> row) {
        return new ArrayList<>(new LinkedHashSet<>(row));
    }

    private static List<Object> fillMissing(Object[] row, String placeholder) {
        return Arrays.stream(row).map(value -> (value == null || value.toString().trim().isEmpty()) ? placeholder : value).collect(Collectors.toList());
    }
}