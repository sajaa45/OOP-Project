package service.DataAnalysis;

import javax.imageio.ImageIO;
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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.List;

public class DataVisualizationAnalysisService {

    private service.DataAnalysis.DataAnalysisService analysis;

    public DataVisualizationAnalysisService(DataAnalysisService analysis) {
        this.analysis = analysis;
    }

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
    public static void saveChartAsImage(JFreeChart chart, String fileName) {
        try {
            int chartWidth = 600;
            int chartHeight = 400;
            BufferedImage chartImage = chart.createBufferedImage(chartWidth, chartHeight);
            File outputFile = new File(fileName);
            if (outputFile.exists()) {
                outputFile.delete();  // Delete the existing file before saving the new one
            }
            ImageIO.write(chartImage, "png", outputFile);

            System.out.println("Saved chart as: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error saving chart: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void createScatterPlotsAndSave(List<Car> cars, List<String> attributes) {
        // Loop through each attribute to create and save individual scatter plots
        for (String xAttribute : attributes) {
            try {
                // Create a scatter plot with regression for the current attribute
                JFreeChart chart = createScatterPlotWithRegression(cars, xAttribute, "price");

                // Save the chart as an image
                String fileName = "data/numerical_data/scatter_plot_" + xAttribute + "_vs_price.png";
                saveChartAsImage(chart, fileName);

            } catch (Exception e) {  // Catching general exception for broader error handling
                System.err.println("Error generating scatter plot for " + xAttribute + ": " + e.getMessage());
                e.printStackTrace();  // Print stack trace for debugging
            }
        }
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

        return scatterPlot;
    }

}