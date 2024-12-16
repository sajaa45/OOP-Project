package interfaces;

import model.Car;

import java.util.List;
import java.util.Map;

public abstract class DataAnalysisInterface {

    public interface DataAnalysisServiceInterface {
        public abstract void analyzeNumericalData(List<Car> cars, int[] numericalIndices, Map<Integer, String> columnNames);
        public abstract void analyzeCategoricalData(List<Car> cars, int[] categoricalIndices, Map<Integer, String> columnNames);
        public abstract double calculateCorrelation(List<Car> cars, String attribute1, String attribute2);
    }

}
