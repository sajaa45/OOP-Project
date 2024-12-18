package utils;

import model.Car;

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

    public static List<Car> cleanCarData(List<Car> cars, String placeholder) {
        return cars.stream().map(car -> cleanCar(car, placeholder)).collect(Collectors.toList());
    }

    private static Car cleanCar(Car car, String placeholder) {
        if (isNullOrEmpty(car.getModel())) {
            car.setModel(placeholder);
        } else {
            car.setModel(car.getModel().trim());
        }
        if (car.getYear() <= 0) {
            car.setYear(0);
        }
        if (car.getPrice() < 0) {
            car.setPrice(0.0);
        }
        if (isNullOrEmpty(car.getTransmission())) {
            car.setTransmission(placeholder);
        } else {
            car.setTransmission(car.getTransmission().trim());
        }
        if (car.getMileage() < 0) {
            car.setMileage(0);
        }
        if (isNullOrEmpty(car.getFuelType())) {
            car.setFuelType(placeholder);
        } else {
            car.setFuelType(car.getFuelType().trim());
        }
        if (car.getRoadTax() < 0) {
            car.setRoadTax(0.0);
        }
        if (car.getMpg() < 0) {
            car.setMpg(0.0);
        }
        if (car.getEngineSize() < 0) {
            car.setEngineSize(0.0);
        }
        if (isNullOrEmpty(car.getTypeCar())) {
            car.setTypeCar(placeholder);
        } else {
            car.setTypeCar(car.getTypeCar().trim());
        }
        return car;
    }

    private static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

}