package repository;

import java.util.ArrayList;
import java.util.List;

import model.Car;

public class CarRepository {
    private List<Car> cars = new ArrayList<>();
    private Object[][] data;

    public void processRawData(List<List<String>> rawData) {
        for (int i = 0; i < rawData.size(); i++) {
            List<String> row = rawData.get(i);
            // Skip the header row
            if (i == 0 || row.get(0).equalsIgnoreCase("model")) {
                continue;
            }
            // Validate row length
            if (row.size() != 10) {
                System.err.println("Invalid row size: " + row);
                continue;
            }
            try {
                // Trim and sanitize each field
                String model = row.get(0).trim();
                int year = parseInteger(row.get(1), "Year");
                double price = parseDouble(row.get(2), "Price");
                String transmission = row.get(3).trim();
                int mileage = parseInteger(row.get(4), "Mileage");
                String fuelType = row.get(5).trim();

                double roadTax = parseDouble(row.get(6), "Road Tax");
                double mpg = parseDouble(row.get(7), "MPG");
                double engineSize = parseDouble(row.get(8), "Engine Size");
                String car_type = row.get(9).trim();

                Car car = new Car(model, year, price, transmission, mileage, fuelType, roadTax, mpg, engineSize, car_type);
                cars.add(car);
            } catch (Exception e) {
                System.err.println("Error processing row: " + row);
                e.printStackTrace();
            }
        }
        populate2DArray();
    }

    private void populate2DArray() {
        int rows = cars.size();
        int cols = 10;
        data = new Object[rows][cols];
        for (int i = 0; i < rows; i++) {
            Car car = cars.get(i);
            // Vehicle fields
            data[i][0] = car.getModel();
            data[i][1] = car.getYear();
            data[i][2] = car.getPrice();
            data[i][3] = car.getTransmission();
            data[i][4] = car.getMileage();
            data[i][5] = car.getFuelType();

            // Car-specific fields
            data[i][6] = car.getRoadTax();
            data[i][7] = car.getMpg();
            data[i][8] = car.getEngineSize();
            data[i][9] = car.getCarType();
        }
    }

    public Object[][] getData() {
        return data;
    }

    public List<Car> getCars() {
        return cars;
    }

    // Helper method to parse integers safely
    private int parseInteger(String value, String columnName) {
        try {
            return Integer.parseInt(value.trim().replace("\"", ""));
        } catch (NumberFormatException e) {
            System.err.println("Invalid integer in column '" + columnName + "': " + value);
            return -1; // Use a default value or handle this case as needed
        }
    }

    // Helper method to parse doubles safely
    private double parseDouble(String value, String columnName) {
        try {
            return Double.parseDouble(value.trim().replace("\"", ""));
        } catch (NumberFormatException e) {
            System.err.println("Invalid double in column '" + columnName + "': " + value);
            return -1.0; // Use a default value or handle this case as needed
        }
    }
}