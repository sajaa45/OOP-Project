package utils;

import java.util.*;
import java.util.stream.Collectors;

public class DataCleaningUtils {
    public static Object[][] cleanData(Object[][] data, String placeholder) {
        Object[][] cleanedData = new Object[data.length][data[0].length];
        for (int i = 0; i < data.length; i++) {
            cleanedData[i] = cleanRow(data[i], placeholder);
        }
        return cleanedData;
    }

    private static Object[] cleanRow(Object[] row, String placeholder) {
        List<Object> rowList = new ArrayList<>(Arrays.asList(row));
        for (int i = 0; i < row.length; i++) {
            if (i == 0 || i == 3 || i == 5) { // Categorical columns
                rowList.set(i, cleanCategoricalColumn(row[i], placeholder));
            } else if (i == 1 || i == 2 || i == 4 || i == 6 || i == 7 || i == 8) {
                if (row[i] != null && !isNumeric(row[i].toString())) {
                    rowList.set(i, placeholder);
                }
            }
        }
        rowList = removeDuplicate(rowList);
        rowList = fillMissing(rowList, placeholder);
        return rowList.toArray();
    }

    private static Object cleanCategoricalColumn(Object value, String placeholder) {
        if (value != null && isNumeric(value.toString())) {
            return placeholder;
        }
        return (value == null || value.toString().trim().isEmpty()) ? placeholder : value.toString().trim();
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }

    }

    public static boolean isAlphabetic(String str) {
        return str != null && str.matches("^[a-zA-Z-_]+$");
    }

    private static List<Object> removeDuplicate(List<Object> row) {
        return new ArrayList<>(new LinkedHashSet<>(row));
    }

    private static List<Object> fillMissing(List<Object> row, String placeholder) {
        return row.stream().map(value -> (value == null || value.toString().trim().isEmpty()) ? placeholder : value).collect(Collectors.toList());
    }

    public static List<Object[]> extractValidPairs(Object[][] data, int col1Index, int col2Index) {
        return Arrays.stream(data).filter(row -> row != null && row.length > Math.max(col1Index, col2Index)).filter(row -> row[col1Index] != null && row[col2Index] != null).filter(row -> (isNumeric(row[col1Index].toString()) || isAlphabetic(row[col1Index].toString())) && (isNumeric(row[col2Index].toString()) || isAlphabetic(row[col2Index].toString()))).collect(Collectors.toList());
    }
    }
