package service;

import model.Car;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.jfree.chart.JFreeChart;

import java.util.*;
import java.util.stream.Collectors;

public class DataAnalysisService {

    public DataAnalysisService(List<Car> carList) {
    }
    List<JFreeChart> allCharts = new ArrayList<>();
    public DataAnalysisService() {
    }
    private void appendOutput(String text) {
        System.out.println(text);
    }


    public void analyzeNumericalData(Object[][] data, int[] numericalIndices, Map<Integer, String> columnNames) {
        for (int index : numericalIndices) {
            double[] columnData = extractDoubleColumn(data, index);
            if (columnData.length == 0) {
                appendOutput("Skipping analysis for column: " + columnNames.getOrDefault(index, "Unknown Column") + " due to lack of valid data.");
                continue;
            }
            String columnName = columnNames.getOrDefault(index, "Unknown Column");
            appendOutput("\nAnalysis for Column: " + columnName);

            Map<String, Double> centralTendency = calculateCentralTendency(columnData);
            appendOutput("Measures of Central Tendency: " + centralTendency);

            Map<String, Double> variability = calculateVariability(columnData);
            appendOutput("Measures of Variability: " + variability);
            JFreeChart histogram = DataVisualizationService.plotHistogram(columnData, "Histogram for " + columnName, "Value", "Frequency");
            allCharts.add(histogram);
        }
    }


    public void analyzeCategoricalData(Object[][] data, int[] categoricalIndices, Map<Integer, String> columnNames) {
        for (int index : categoricalIndices) {
            String[] columnData = extractStringColumn(data, index);
            String columnName = columnNames.getOrDefault(index, "Column " + index);
            appendOutput("\nAnalysis for " + columnName + ":");

            if (columnData.length == 0) {
                appendOutput("No data found for column " + columnName);
                continue;
            }

            Map<String, Integer> frequency = calculateFrequency(columnData);
            appendOutput("Frequency Distribution: " + frequency);
            appendOutput("Unique Categories: " + frequency.keySet());
            appendOutput("Proportions: " + calculateProportions(frequency, columnData.length));

            JFreeChart pieChart = DataVisualizationService.plotPieChart(frequency, "Pie Chart for " + columnName);
            allCharts.add(pieChart);
        }
        DataVisualizationService.displayChart(allCharts.toArray(new JFreeChart[0]));
    }

    private double[] extractDoubleColumn(Object[][] data, int columnIndex) {
        return Arrays.stream(data).filter(row -> row != null && row.length > columnIndex && row[columnIndex] != null).mapToDouble(row -> {
            try {
                return Double.parseDouble(row[columnIndex].toString());
            } catch (NumberFormatException e) {
                return Double.NaN;
            }
        }).filter(Double::isFinite).toArray();
    }

    private String[] extractStringColumn(Object[][] data, int columnIndex) {
        return Arrays.stream(data).filter(row -> row != null && row.length > columnIndex && row[columnIndex] != null).map(row -> row[columnIndex].toString()).toArray(String[]::new);
    }

    private Map<String, Double> calculateCentralTendency(double[] data) {
        DescriptiveStatistics stats = new DescriptiveStatistics(data);
        Map<String, Double> result = new HashMap<>();
        result.put("Mean", stats.getMean());
        result.put("Median", calculateMedian(data));
        result.put("Mode", calculateMode(data));
        return result;
    }

    private Map<String, Double> calculateVariability(double[] data) {
        DescriptiveStatistics stats = new DescriptiveStatistics(data);
        Map<String, Double> result = new HashMap<>();
        result.put("Range", stats.getMax() - stats.getMin());
        result.put("Variance", stats.getVariance());
        result.put("Standard Deviation", stats.getStandardDeviation());
        result.put("Interquartile Range", calculateIQR(data));
        return result;
    }

    private double calculateMedian(double[] data) {
        if (data.length == 0) {
            appendOutput("Error: Data is empty, cannot calculate median.");
            return Double.NaN;
        }
        Arrays.sort(data);
        int middle = data.length / 2;
        return (data.length % 2 == 0) ? (data[middle - 1] + data[middle]) / 2.0 : data[middle];
    }

    private double calculateMode(double[] data) {
        Map<Double, Long> frequency = Arrays.stream(data).boxed().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
        return frequency.entrySet().stream().max(Map.Entry.comparingByValue()).orElseThrow(() -> new IllegalStateException("No mode found")).getKey();
    }

    private double calculateIQR(double[] data) {
        Arrays.sort(data);
        int q1Index = data.length / 4;
        int q3Index = 3 * data.length / 4;
        return data[q3Index] - data[q1Index];
    }

    private Map<String, Integer> calculateFrequency(String[] data) {
        Map<String, Integer> frequency = new HashMap<>();
        for (String value : data) {
            frequency.put(value, frequency.getOrDefault(value, 0) + 1);
        }
        return frequency;
    }

    private Map<String, Double> calculateProportions(Map<String, Integer> frequency, int total) {
        return frequency.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue() / (double) total));
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
