package service;

import javax.swing.*;
import model.Car;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.data.statistics.HistogramDataset;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.List;

public class DataVisualizationService {
    private DataAnalysisService analysis;
    public static JFreeChart plotHistogram(double[] data, String title, String xLabel, String yLabel) {
        HistogramDataset dataset = new HistogramDataset();
        dataset.addSeries("Frequency", data, 10);

        JFreeChart histogram = ChartFactory.createHistogram(title, xLabel, yLabel, dataset, PlotOrientation.VERTICAL, false, false, false);
        return histogram;
    }

    public static JFreeChart plotPieChart(Map<String, Integer> frequencyMap, String title) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        frequencyMap.forEach(dataset::setValue);
        JFreeChart pieChart = ChartFactory.createPieChart(title, dataset, true, true, false);
        PiePlot plot = (PiePlot) pieChart.getPlot();
        PieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator("{0}: {1} ({2})", new DecimalFormat("0"), new DecimalFormat("0%"));
        plot.setLabelGenerator(labelGenerator);
        return pieChart;
    }

    public static void displayChart(JFreeChart[] charts) {
        int numCharts = charts.length;

        if (numCharts == 0) {
            System.out.println("No charts to display.");
            return;
        }
        int rows = (int) Math.ceil(Math.sqrt(numCharts));
        int cols = (int) Math.ceil((double) numCharts / rows);
        if (rows == 0 || cols == 0) {
            System.out.println("Invalid grid layout parameters.");
            return;
        }
        JFrame chartFrame = new JFrame("Chart Display");
        chartFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chartFrame.setLayout(new GridLayout(rows, cols));
        for (JFreeChart chart : charts) {
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(400, 300));
            chartFrame.add(chartPanel);
        }
        chartFrame.pack();
        chartFrame.setVisible(true);
    }

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
//    creating scatter plot with regression
    public void createScatterPlotWithRegression(List<Car> cars, String xAttribute, String yAttribute) {
        XYSeries scatterSeries = new XYSeries("Data Points");

        for (Car car : cars) {
            double xValue = analysis.getAttributeValue(car, xAttribute);
            double yValue = analysis.getAttributeValue(car, yAttribute);
            scatterSeries.add(xValue, yValue);
        }

        XYSeriesCollection dataset = new XYSeriesCollection(scatterSeries);

        double minX = scatterSeries.getMinX();
        double maxX = scatterSeries.getMaxX();
        XYSeries regressionSeries = new XYSeries("Regression Line");
        regressionSeries.add(minX, analysis.predict(minX));
        regressionSeries.add(maxX, analysis.predict(maxX));
        dataset.addSeries(regressionSeries);

        JFreeChart scatterPlot = ChartFactory.createScatterPlot(
                yAttribute + " vs " + xAttribute,
                xAttribute,
                yAttribute,
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        JFrame frame = new JFrame("Scatter Plot with Regression");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new ChartPanel(scatterPlot));
        frame.pack();
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
