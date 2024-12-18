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
            // Validate row length (now 10 to include type_car)
            if (row.size() != 10) {
                System.err.println("Invalid row size at index " + i + ": " + row);
                continue;
            }
            try {
                String model = row.get(0).trim(); // Trim leading/trailing spaces
                int year = Integer.parseInt(removeExtraQuotes(row.get(1).trim())); // Trim and parse year
                double price = Double.parseDouble(removeExtraQuotes(row.get(2).trim())); // Trim and parse price
                String transmission = row.get(3).trim(); // Trim transmission
                int mileage = Integer.parseInt(removeExtraQuotes(row.get(4).trim())); // Trim and parse mileage
                String fuelType = row.get(5).trim(); // Trim fuel type
                double roadTax = Double.parseDouble(removeExtraQuotes(row.get(6).trim())); // Trim and parse road tax
                double mpg = Double.parseDouble(removeExtraQuotes(row.get(7).trim())); // Trim and parse mpg
                double engineSize = Double.parseDouble(removeExtraQuotes(row.get(8).trim())); // Trim and parse engine size
                String typeCar = row.get(9).trim(); // Trim type_car

                Car car = new Car(model, year, price, mileage, fuelType, transmission, roadTax, mpg, engineSize, typeCar);
                cars.add(car);
            } catch (NumberFormatException e) {
                System.err.println("Number format error processing row at index " + i + ": " + row);
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("Error processing row at index " + i + ": " + row);
                e.printStackTrace();
            }
        }
        populate2DArray();
    }

    // Helper method to remove extra quotes
    private String removeExtraQuotes(String value) {
        return value.replaceAll("^\"|\"$", "").trim(); // Remove leading and trailing quotes
    }
    private void populate2DArray() {
        int rows = cars.size();
        int cols = 10; // Update column count to 10 to include type_car
        data = new Object[rows][cols];
        for (int i = 0; i < rows; i++) {
            Car car = cars.get(i);
            data[i][0] = car.getModel();
            data[i][1] = car.getYear();
            data[i][2] = car.getPrice();
            data[i][3] = car.getMileage();
            data[i][4] = car.getFuelType();
            data[i][5] = car.getTransmission();
            data[i][6] = car.getRoadTax();
            data[i][7] = car.getMpg();
            data[i][8] = car.getEngineSize();
            data[i][9] = car.getTypeCar(); // Add type_car to the array
        }
    }

    public Object[][] getData() {
        return data;
    }

    public List<Car> getCars() {
        return cars;
    }
}