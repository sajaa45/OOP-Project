package org.example.Repository;
import java.util.ArrayList;
import java.util.List;
import org.example.Model.Car;

public class CarRepository {

    private List<Car> cars = new ArrayList<>();
    private Object[][] data;

    public void processRawData(List<String[]> rawData) {
        for (String[] row : rawData) {
            try {
                String make = row[0];
                String model = row[1];
                int year = Integer.parseInt(row[2]);
                double price = Double.parseDouble(row[3]);
                int mileage = Integer.parseInt(row[4]);
                String fuelType = row[5];
                String transmission = row[6];
                double roadTax = Double.parseDouble(row[7]);
                double mpg = Double.parseDouble(row[8]);
                double engineSize = Double.parseDouble(row[9]);

                Car car = new Car(make, model, year, price, mileage, fuelType, transmission, roadTax, mpg, engineSize);
                cars.add(car);
            } catch (Exception e) {
                System.err.println("Error processing row: " + String.join(", ", row));
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
            data[i][0] = car.getMake();
            data[i][1] = car.getModel();
            data[i][2] = car.getYear();
            data[i][3] = car.getPrice();
            data[i][4] = car.getMileage();
            data[i][5] = car.getFuelType();
            data[i][6] = car.getTransmission();
            data[i][7] = car.getRoadTax();
            data[i][8] = car.getMpg();
            data[i][9] = car.getEngineSize();
        }
    }

    public Object[][] getData() {
        return data;
    }

    public List<Car> getCars() {
        return cars;
    }
}

