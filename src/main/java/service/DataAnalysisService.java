package service;

import model.Car;
import java.util.List;

public class DataAnalysisService {

    public DataAnalysisService(List<Car> carList) {
    }

    // Method to calculate correlation coefficient
    public double calculateCorrelation(List<Car> cars, String attribute1, String attribute2) {
        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0, sumY2 = 0;
        int n = cars.size();

        for (Car car : cars) {
            double x = getAttributeValue(car, attribute1);
            double y = getAttributeValue(car, attribute2);
            sumX += x;
            sumY += y;
            sumXY += x * y;
            sumX2 += x * x;
            sumY2 += y * y;
        }

        double numerator = (n * sumXY) - (sumX * sumY);
        double denominator = Math.sqrt((n * sumX2 - sumX * sumX) * (n * sumY2 - sumY * sumY));

        return denominator == 0 ? 0 : numerator / denominator; // Return 0 if denominator is 0
    }

    // Method to get attribute value based on attribute name
    public double getAttributeValue(Car car, String attribute) {
        switch (attribute.toLowerCase()) { // Use toLowerCase for case-insensitive matching
            case "year":
                return car.getYear();
            case "price":
                return car.getPrice();
            case "mileage":
                return car.getMileage();
            case "roadtax": // Note: Changed to "roadtax" to match the case
                return car.getRoadTax();
            case "mpg":
                return car.getMpg();
            case "enginesize": // Note: Changed to "enginesize" to match the case
                return car.getEngineSize();
            default:
                throw new IllegalArgumentException("Invalid attribute: " + attribute);
        }
        }
    }
