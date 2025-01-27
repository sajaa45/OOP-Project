package service.DataAnalysis;

import model.Car;
import interfaces.AdvancedDataAnalysis;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.jfree.chart.JFreeChart;

import java.util.*;
import java.util.stream.Collectors;

public class DataAnalysisService implements AdvancedDataAnalysis {

    private double regressionSlope;
    private double regressionIntercept;
    private double rSquared;
    private double sst, ssr, sse;
    private double msr, mse, fStat;
    private List<JFreeChart> allCharts = new ArrayList<>();

    public DataAnalysisService() {
    }

    private void appendOutput(String text) {
        System.out.println(text);
    }

    public void performRegression(List<Car> cars, String xAttribute, String yAttribute) {
        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;
        int n = cars.size();

        for (Car car : cars) {
            double x = getAttributeValue(car, xAttribute);
            double y = getAttributeValue(car, yAttribute);
            sumX += x;
            sumY += y;
            sumXY += x * y;
            sumX2 += x * x;
        }

        double meanX = sumX / n;
        double meanY = sumY / n;

        regressionSlope = (sumXY - n * meanX * meanY) / (sumX2 - n * meanX * meanX);
        regressionIntercept = meanY - regressionSlope * meanX;

        sst = 0; ssr = 0; sse = 0;
        for (Car car : cars) {
            double x = getAttributeValue(car, xAttribute);
            double y = getAttributeValue(car, yAttribute);
            double predictedY = predict(x);

            sst += Math.pow(y - meanY, 2);
            ssr += Math.pow(predictedY - meanY, 2);
            sse += Math.pow(y - predictedY, 2);
        }
        rSquared = ssr / sst;
        msr = ssr;
        mse = sse / (n - 2);
        fStat = mse != 0 ? msr / mse : Double.NaN;
    }
    public double predict(double x) {
        return regressionSlope * x + regressionIntercept;
    }

    public String getAnovaTable() {
        return String.format(
                "ANOVA Table:\n" +
                        "--------------------------------\n" +
                        "Source      | SS      | MS      | F\n" +
                        "Regression  | %.4f | %.4f | %.4f\n" +
                        "Residual    | %.4f | %.4f | -\n" +
                        "Total       | %.4f | -       | -\n",
                ssr, msr, fStat, sse, mse, sst
        );
    }

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

        return denominator == 0 ? 0 : numerator / denominator;
    }

    public double getAttributeValue(Car car, String attribute) {
        switch (attribute.toLowerCase()) {
            case "year": return car.getYear();
            case "price": return car.getPrice();
            case "mileage": return car.getMileage();
            case "roadtax": return car.getRoadTax();
            case "mpg": return car.getMpg();
            case "enginesize": return car.getEngineSize();
            default: throw new IllegalArgumentException("Invalid attribute: " + attribute);
        }
    }

    public void analyzeNumericalData(List<Car> carList, String[] numericalAttributes) {
        for (String attribute : numericalAttributes) {
            double[] columnData = extractDoubleColumn(carList, attribute);
            if (columnData.length == 0) {
                appendOutput("No numerical data found for attribute: " + attribute);
                continue;
            }
            appendOutput("\nAnalysis for Attribute: " + attribute);
            Map<String, Double> centralTendency = calculateCentralTendency(columnData);
            appendOutput("Measures of Central Tendency: " + centralTendency);
            Map<String, Double> variability = calculateVariability(columnData);
            appendOutput("Measures of Variability: " + variability);
            JFreeChart histogram = DataVisualizationAnalysisService.plotHistogram(columnData, "Histogram for " + attribute, "Value", "Frequency");
            allCharts.add(histogram);

            // Save the histogram plot as an image
            String fileName = "data/categorical_data/histogram_" + attribute + ".png";
            DataVisualizationAnalysisService.saveChartAsImage(histogram, fileName);
        }
    }


    public void analyzeCategoricalData(List<Car> carList, String[] categoricalAttributes) {
        for (String attribute : categoricalAttributes) {
            String[] columnData = extractCategoricalColumn(carList, attribute);
            appendOutput("\nAnalysis for Attribute: " + attribute);
            if (columnData.length == 0) {
                appendOutput("No data available for attribute: " + attribute);
                continue;
            }
            Map<String, Integer> frequency = calculateFrequency(columnData);
            appendOutput("Frequency Distribution: " + frequency);
            appendOutput("Proportions: " + calculateProportions(frequency, columnData.length));
            JFreeChart pieChart = DataVisualizationAnalysisService.plotPieChart(frequency, "Pie Chart for " + attribute);
            allCharts.add(pieChart);

            // Save the pie chart as an image
            String fileName = "C:\\Users\\LENOVO\\Desktop\\Junior\\project_oop_version2\\data\\categorical_data\\pie_chart_" + attribute + ".png";
            DataVisualizationAnalysisService.saveChartAsImage(pieChart, fileName);
        }
    }


    private double[] extractDoubleColumn(List<Car> carList, String attribute) {
        return carList.stream()
                .mapToDouble(car -> {
                    switch (attribute) {
                        case "price": return car.getPrice();
                        case "year": return car.getYear();
                        case "mileage": return car.getMileage();
                        case "roadTax": return car.getRoadTax();
                        case "mpg": return car.getMpg();
                        case "engineSize": return car.getEngineSize();
                        default: return Double.NaN;
                    }
                })
                .filter(Double::isFinite)
                .toArray();
    }

    private String[] extractCategoricalColumn(List<Car> carList, String attribute) {
        return carList.stream()
                .map(car -> {
                    switch (attribute) {
                        case "model": return car.getModel();
                        case "transmission": return car.getTransmission();
                        case "fuelType": return car.getFuelType();
                        case "typeCar": return car.getCarType();
                        default: return "";
                    }
                })
                .filter(value -> !isNumeric(value))
                .toArray(String[]::new);
    }

    private boolean isNumeric(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
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
        Map<Double, Long> frequency = Arrays.stream(data)
                .boxed()
                .collect(Collectors.groupingBy(e -> e, Collectors.counting()));
        return frequency.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow(() -> new IllegalStateException("No mode found"))
                .getKey();
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
        return frequency.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue() / (double) total));
    }

}