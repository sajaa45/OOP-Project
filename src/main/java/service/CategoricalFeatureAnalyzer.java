package service;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import model.Car;

public class CategoricalFeatureAnalyzer {

    public Map<String, Long> analyze(List<Car> cars, String featureName) {
        return cars.stream()
                .map(car -> getCarProperty(car, featureName))
                .filter(value -> value != null && !value.isEmpty())
                .collect(Collectors.groupingBy(value -> value, Collectors.counting()));
    }

    public void visualize(Map<String, Long> data, String featureName) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        data.forEach((key, value) -> dataset.addValue(value, featureName, key));

        JFreeChart barChart = ChartFactory.createBarChart(
                "Categorical Feature Analysis: " + featureName,
                "Category",
                "Count",
                dataset
        );

        JFrame frame = new JFrame("Categorical Feature Visualization");
        frame.setContentPane(new ChartPanel(barChart));
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    private String getCarProperty(Car car, String featureName) {
        switch (featureName.toLowerCase()) {
            case "model": return car.getModel();
            case "fueltype": return car.getFuelType();
            case "transmission": return car.getTransmission();
            default: return null;
        }
    }
}

