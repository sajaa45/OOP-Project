package interfaces;

import model.Car;

import java.util.List;

public interface AdvancedDataAnalysis extends GeneralDataAnalysis {

    void performRegression(List<Car> cars, String xAttribute, String yAttribute);
    double predict(double x);
    String getAnovaTable();
}