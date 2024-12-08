package service;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.statistics.HistogramDataset;

import javax.swing.*;
import java.util.List;
import java.util.DoubleSummaryStatistics;
import model.Car;

public class NumericalFeatureAnalyzer {
    public DoubleSummaryStatistics analyze(List<Car> cars, String featureName) {
        return cars.stream()
                .mapToDouble(car -> getCarNumericProperty(car, featureName))
                .summaryStatistics();
    }

    public void visualize(List<Car> cars, String featureName) {
        double[] values = cars.stream()
                .mapToDouble(car -> getCarNumericProperty(car, featureName))
                .toArray();

        HistogramDataset dataset = new HistogramDataset();
        dataset.addSeries(featureName, values, 10);

        JFreeChart histogram = ChartFactory.createHistogram(
                "Numerical Feature Analysis: " + featureName,
                featureName,
                "Frequency",
                dataset
        );

        JFrame frame = new JFrame("Numerical Feature Visualization");
        frame.setContentPane(new ChartPanel(histogram));
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    private double getCarNumericProperty(Car car, String featureName) {
        switch (featureName.toLowerCase()) {
            case "price": return car.getPrice();
            case "year": return car.getYear();
            case "mileage": return car.getMileage();
            case "roadtax": return car.getRoadTax();
            case "mpg": return car.getMpg();
            case "enginesize": return car.getEngineSize();
            default: throw new IllegalArgumentException("Invalid feature name: " + featureName);
        }
    }
}

