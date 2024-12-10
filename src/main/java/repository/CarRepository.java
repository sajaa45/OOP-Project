package repository;

import java.util.ArrayList;
import java.util.Arrays;
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
            if (row.size() != 9) {
                System.err.println("Invalid row size: " + row);
                continue;
            }
            try {
                String model = row.get(0);
                int year = Integer.parseInt(row.get(1));
                double price = Double.parseDouble(row.get(2));
                String transmission = row.get(3);
                int mileage = Integer.parseInt(row.get(4));
                String fuelType = row.get(5);
                double roadTax = Double.parseDouble(row.get(6));
                double mpg = Double.parseDouble(row.get(7));
                double engineSize = Double.parseDouble(row.get(8));

                Car car = new Car(model, year, price, transmission, mileage, fuelType, roadTax, mpg, engineSize);
                cars.add(car);
            } catch (Exception e) {
                System.err.println("Error processing row: " + row);
                e.printStackTrace();
            }
        }
        populate2DArray();
    }

    public void populate2DArray() {
        int rows = cars.size();
        int cols = 9;
        data = new Object[rows][cols];
        for (int i = 0; i < rows; i++) {
            Car car = cars.get(i);
            data[i][0] = car.getModel();
            data[i][1] = car.getYear();
            data[i][2] = car.getPrice();
            data[i][3] = car.getTransmission();
            data[i][4] = car.getMileage();
            data[i][5] = car.getFuelType();
            data[i][6] = car.getRoadTax();
            data[i][7] = car.getMpg();
            data[i][8] = car.getEngineSize();
        }
    }

    public Object[][] getData() {
        return data;
    }
    public List<Car> getCars() {
        return cars;
    }
    public String[] getHeaders() {
        return new String[] {"Model", "Year", "Price", "Transmission", "Mileage", "Fuel Type", "Road Tax", "MPG", "Engine Size"};
    }
}
