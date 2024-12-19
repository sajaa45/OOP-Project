package interfaces;

import model.Car;

import java.util.List;
import java.util.Map;

public interface GeneralDataAnalysis {

    void analyzeNumericalData(List<Car> carList, String[] numericalAttributes);
    void analyzeCategoricalData(List<Car> carList, String[] categoricalAttributes);
    double calculateCorrelation(List<Car> cars, String attribute1, String attribute2);
    double getAttributeValue(Car car, String attribute);
}