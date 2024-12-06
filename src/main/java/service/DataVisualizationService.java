package service;

import javax.swing.*;
import model.Car;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.util.List;

public class DataVisualizationService {
    private DataAnalysisService analysis;

    // Constructor that accepts DataAnalysisService
    public DataVisualizationService(DataAnalysisService analysis) {
        this.analysis = analysis;
    }

    // Method to create a scatter plot matrix
    public void createScatterPlotMatrix(List<Car> cars, List<String> attributes) {
        int numAttributes = attributes.size();
        JFrame frame = new JFrame("Scatter Plot Matrix");
        frame.setLayout(new GridLayout(numAttributes, numAttributes)); // Grid layout for the matrix

        for (int row = 0; row < numAttributes; row++) {
            for (int col = 0; col < numAttributes; col++) {
                String xAttribute = attributes.get(col);
                String yAttribute = attributes.get(row);

                // Create scatter plot for the current attribute pair
                JFreeChart chart = createScatterPlot(cars, xAttribute, yAttribute);
                ChartPanel chartPanel = new ChartPanel(chart);
                chartPanel.setPreferredSize(new Dimension(200, 200)); // Set size for each plot
                frame.add(chartPanel); // Add the plot to the frame
            }
        }

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Ensure the application exits on close
        frame.setVisible(true);
    }

    // Method to create a single scatter plot
    private JFreeChart createScatterPlot(List<Car> cars, String xAttribute, String yAttribute) {
        XYSeries series = new XYSeries(yAttribute + " vs " + xAttribute);
        for (Car car : cars) {
            double xValue = analysis.getAttributeValue(car, xAttribute);
            double yValue = analysis.getAttributeValue(car, yAttribute);
            series.add(xValue, yValue);
        }

        XYSeriesCollection dataset = new XYSeriesCollection(series);
        return ChartFactory.createScatterPlot(
                yAttribute + " vs " + xAttribute, // Title
                xAttribute, // X-Axis label
                yAttribute, // Y-Axis label
                dataset,
                PlotOrientation.VERTICAL,
                false, // No legend for individual plots
                true,
                false
        );
    }
}
