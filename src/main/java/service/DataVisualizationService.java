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

    public DataVisualizationService(DataAnalysisService analysis) {
        this.analysis = analysis;
    }

    public void createScatterPlotMatrix(List<Car> cars, List<String> attributes) {
        int numAttributes = attributes.size();
        JFrame frame = new JFrame("Scatter Plot Matrix");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel matrixPanel = new JPanel();
        matrixPanel.setLayout(new GridLayout(numAttributes, numAttributes));

        for (int row = 0; row < numAttributes; row++) {
                String xAttribute = attributes.get(row);
                JFreeChart chart = createScatterPlotWithRegression(cars, xAttribute, "price");
                ChartPanel chartPanel = new ChartPanel(chart);
                chartPanel.setPreferredSize(new Dimension(400, 300)); // Set a fixed size for each chart
                matrixPanel.add(chartPanel);

        }
        matrixPanel.setPreferredSize(new Dimension(300 * numAttributes, 300 * numAttributes));


        frame.add(matrixPanel);
        frame.pack();
        frame.setLocationRelativeTo(null); // Center the frame
        frame.setVisible(true);
    }

    public JFreeChart createScatterPlotWithRegression(List<Car> cars, String xAttribute, String yAttribute) {
        XYSeries scatterSeries = new XYSeries("Data Points");
        double minX = Double.MAX_VALUE, maxX = Double.MIN_VALUE;

        for (Car car : cars) {
            double x = analysis.getAttributeValue(car, xAttribute);
            double y = analysis.getAttributeValue(car, yAttribute);
            scatterSeries.add(x, y);
            minX = Math.min(minX, x);
            maxX = Math.max(maxX, x);
        }

        XYSeries regressionSeries = new XYSeries("Regression Line");
        for (double x = minX; x <= maxX; x += (maxX - minX) / 100) {
            regressionSeries.add(x, analysis.predict(x));
        }
        analysis.performRegression(cars, xAttribute, yAttribute);
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(scatterSeries);
        dataset.addSeries(regressionSeries);

        JFreeChart scatterPlot = ChartFactory.createScatterPlot(
                "Scatter Plot with Regression: " + yAttribute + " vs " + xAttribute,
                xAttribute, yAttribute, dataset, PlotOrientation.VERTICAL, true, true, false);

        return scatterPlot; // Return the chart instead of displaying it
    }

}