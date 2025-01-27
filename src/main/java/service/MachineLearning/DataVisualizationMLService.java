package service.MachineLearning;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;


public class DataVisualizationMLService {

    // Method to display model results in charts within a JFrame
    public void displayModelResultsInJFrame(List<MLModelTrainer.ModelResult> results) {
        // Create datasets for each metric
        DefaultCategoryDataset mseDataset = new DefaultCategoryDataset();
        DefaultCategoryDataset maeDataset = new DefaultCategoryDataset();
        DefaultCategoryDataset rmseDataset = new DefaultCategoryDataset();
        DefaultCategoryDataset mapeDataset = new DefaultCategoryDataset();
        DefaultCategoryDataset medaeDataset = new DefaultCategoryDataset();
        DefaultCategoryDataset r2Dataset = new DefaultCategoryDataset();

        // Populate datasets with results
        for (MLModelTrainer.ModelResult result : results) {
            String modelName = result.getModelName();
            mseDataset.addValue(result.getMse(), "MSE", modelName);
            maeDataset.addValue(result.getMae(), "MAE", modelName);
            rmseDataset.addValue(result.getRmse(), "RMSE", modelName);
            mapeDataset.addValue(result.getMape(), "MAPE", modelName);
            medaeDataset.addValue(result.getMedae(), "MedAE", modelName);
            r2Dataset.addValue(result.getR2(), "R²", modelName);
        }

        // Create charts for each metric
        JFreeChart mseChart = ChartFactory.createBarChart(
                "MSE by Model", "Model", "MSE", mseDataset, PlotOrientation.VERTICAL, true, true, false
        );
        JFreeChart maeChart = ChartFactory.createBarChart(
                "MAE by Model", "Model", "MAE", maeDataset, PlotOrientation.VERTICAL, true, true, false
        );
        JFreeChart rmseChart = ChartFactory.createBarChart(
                "RMSE by Model", "Model", "RMSE", rmseDataset, PlotOrientation.VERTICAL, true, true, false
        );
        JFreeChart mapeChart = ChartFactory.createBarChart(
                "MAPE by Model", "Model", "MAPE", mapeDataset, PlotOrientation.VERTICAL, true, true, false
        );
        JFreeChart medaeChart = ChartFactory.createBarChart(
                "MedAE by Model", "Model", "MedAE", medaeDataset, PlotOrientation.VERTICAL, true, true, false
        );
        JFreeChart r2Chart = ChartFactory.createBarChart(
                "R² by Model", "Model", "R²", r2Dataset, PlotOrientation.VERTICAL, true, true, false
        );

        // Create panels for each chart
        ChartPanel mseChartPanel = new ChartPanel(mseChart);
        ChartPanel maeChartPanel = new ChartPanel(maeChart);
        ChartPanel rmseChartPanel = new ChartPanel(rmseChart);
        ChartPanel mapeChartPanel = new ChartPanel(mapeChart);
        ChartPanel medaeChartPanel = new ChartPanel(medaeChart);
        ChartPanel r2ChartPanel = new ChartPanel(r2Chart);

        // Create a JFrame to display the charts
        JFrame frame = new JFrame("Model Results - Metrics Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1500, 1000);
        frame.setLayout(new GridLayout(3, 2)); // 3 rows, 2 columns

        // Add charts to the frame
        frame.add(mseChartPanel);
        frame.add(maeChartPanel);
        frame.add(rmseChartPanel);
        frame.add(mapeChartPanel);
        frame.add(medaeChartPanel);
        frame.add(r2ChartPanel);

        // Display the frame
        frame.setVisible(true);
    }

    // Method to display evaluation and testing results in charts within a JFrame
    public void displayEvaluationAndTestResults(MLModelTrainer.ModelResult validationResult, MLModelTrainer.ModelResult testResult) {
        // Create datasets for each metric
        DefaultCategoryDataset mseDataset = new DefaultCategoryDataset();
        DefaultCategoryDataset maeDataset = new DefaultCategoryDataset();
        DefaultCategoryDataset rmseDataset = new DefaultCategoryDataset();
        DefaultCategoryDataset mapeDataset = new DefaultCategoryDataset();
        DefaultCategoryDataset medaeDataset = new DefaultCategoryDataset();
        DefaultCategoryDataset r2Dataset = new DefaultCategoryDataset();

        // Populate datasets with validation and test results
        mseDataset.addValue(validationResult.getMse(), "Validation", "MSE");
        mseDataset.addValue(testResult.getMse(), "Test", "MSE");

        maeDataset.addValue(validationResult.getMae(), "Validation", "MAE");
        maeDataset.addValue(testResult.getMae(), "Test", "MAE");

        rmseDataset.addValue(validationResult.getRmse(), "Validation", "RMSE");
        rmseDataset.addValue(testResult.getRmse(), "Test", "RMSE");

        mapeDataset.addValue(validationResult.getMape(), "Validation", "MAPE");
        mapeDataset.addValue(testResult.getMape(), "Test", "MAPE");

        medaeDataset.addValue(validationResult.getMedae(), "Validation", "MedAE");
        medaeDataset.addValue(testResult.getMedae(), "Test", "MedAE");

        r2Dataset.addValue(validationResult.getR2(), "Validation", "R²");
        r2Dataset.addValue(testResult.getR2(), "Test", "R²");

        // Create charts for each metric
        JFreeChart mseChart = createBarChart(mseDataset, "MSE (Validation vs Test)", "Dataset", "MSE");
        JFreeChart maeChart = createBarChart(maeDataset, "MAE (Validation vs Test)", "Dataset", "MAE");
        JFreeChart rmseChart = createBarChart(rmseDataset, "RMSE (Validation vs Test)", "Dataset", "RMSE");
        JFreeChart mapeChart = createBarChart(mapeDataset, "MAPE (Validation vs Test)", "Dataset", "MAPE");
        JFreeChart medaeChart = createBarChart(medaeDataset, "MedAE (Validation vs Test)", "Dataset", "MedAE");
        JFreeChart r2Chart = createBarChart(r2Dataset, "R² (Validation vs Test)", "Dataset", "R²");

        // Create panels for each chart
        ChartPanel mseChartPanel = new ChartPanel(mseChart);
        ChartPanel maeChartPanel = new ChartPanel(maeChart);
        ChartPanel rmseChartPanel = new ChartPanel(rmseChart);
        ChartPanel mapeChartPanel = new ChartPanel(mapeChart);
        ChartPanel medaeChartPanel = new ChartPanel(medaeChart);
        ChartPanel r2ChartPanel = new ChartPanel(r2Chart);

        // Create a JFrame to display the charts
        JFrame frame = new JFrame("Evaluation and Testing Results - Metrics Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1500, 1000);
        frame.setLayout(new GridLayout(3, 2)); // 3 rows, 2 columns

        // Add charts to the frame
        frame.add(mseChartPanel);
        frame.add(maeChartPanel);
        frame.add(rmseChartPanel);
        frame.add(mapeChartPanel);
        frame.add(medaeChartPanel);
        frame.add(r2ChartPanel);

        // Display the frame
        frame.setVisible(true);
    }

    // Helper method to create a bar chart
    private JFreeChart createBarChart(DefaultCategoryDataset dataset, String title, String xAxisLabel, String yAxisLabel) {
        return ChartFactory.createBarChart(
                title, xAxisLabel, yAxisLabel, dataset, PlotOrientation.VERTICAL, true, true, false
        );
    }

    // Method to display comparison results in a new JFrame
    public void displayComparisonResults(MLModelTrainer.ModelResult originalValidationResult,
                                         MLModelTrainer.ModelResult originalTestResult,
                                         MLModelTrainer.ModelResult tunedValidationResult,
                                         MLModelTrainer.ModelResult tunedTestResult) {
        // Create datasets for each metric
        DefaultCategoryDataset mseDataset = new DefaultCategoryDataset();
        DefaultCategoryDataset maeDataset = new DefaultCategoryDataset();
        DefaultCategoryDataset rmseDataset = new DefaultCategoryDataset();
        DefaultCategoryDataset mapeDataset = new DefaultCategoryDataset();
        DefaultCategoryDataset medaeDataset = new DefaultCategoryDataset();
        DefaultCategoryDataset r2Dataset = new DefaultCategoryDataset();

        // Populate datasets with original and tuned results
        mseDataset.addValue(originalValidationResult.getMse(), "Original", "Validation MSE");
        mseDataset.addValue(tunedValidationResult.getMse(), "Tuned", "Validation MSE");
        mseDataset.addValue(originalTestResult.getMse(), "Original", "Test MSE");
        mseDataset.addValue(tunedTestResult.getMse(), "Tuned", "Test MSE");

        maeDataset.addValue(originalValidationResult.getMae(), "Original", "Validation MAE");
        maeDataset.addValue(tunedValidationResult.getMae(), "Tuned", "Validation MAE");
        maeDataset.addValue(originalTestResult.getMae(), "Original", "Test MAE");
        maeDataset.addValue(tunedTestResult.getMae(), "Tuned", "Test MAE");

        rmseDataset.addValue(originalValidationResult.getRmse(), "Original", "Validation RMSE");
        rmseDataset.addValue(tunedValidationResult.getRmse(), "Tuned", "Validation RMSE");
        rmseDataset.addValue(originalTestResult.getRmse(), "Original", "Test RMSE");
        rmseDataset.addValue(tunedTestResult.getRmse(), "Tuned", "Test RMSE");

        mapeDataset.addValue(originalValidationResult.getMape(), "Original", "Validation MAPE");
        mapeDataset.addValue(tunedValidationResult.getMape(), "Tuned", "Validation MAPE");
        mapeDataset.addValue(originalTestResult.getMape(), "Original", "Test MAPE");
        mapeDataset.addValue(tunedTestResult.getMape(), "Tuned", "Test MAPE");

        medaeDataset.addValue(originalValidationResult.getMedae(), "Original", "Validation MedAE");
        medaeDataset.addValue(tunedValidationResult.getMedae(), "Tuned", "Validation MedAE");
        medaeDataset.addValue(originalTestResult.getMedae(), "Original", "Test MedAE");
        medaeDataset.addValue(tunedTestResult.getMedae(), "Tuned", "Test MedAE");

        r2Dataset.addValue(originalValidationResult.getR2(), "Original", "Validation R²");
        r2Dataset.addValue(tunedValidationResult.getR2(), "Tuned", "Validation R²");
        r2Dataset.addValue(originalTestResult.getR2(), "Original", "Test R²");
        r2Dataset.addValue(tunedTestResult.getR2(), "Tuned", "Test R²");

        // Create charts for each metric
        JFreeChart mseChart = createBarChart(mseDataset, "MSE Comparison", "Dataset", "MSE");
        JFreeChart maeChart = createBarChart(maeDataset, "MAE Comparison", "Dataset", "MAE");
        JFreeChart rmseChart = createBarChart(rmseDataset, "RMSE Comparison", "Dataset", "RMSE");
        JFreeChart mapeChart = createBarChart(mapeDataset, "MAPE Comparison", "Dataset", "MAPE");
        JFreeChart medaeChart = createBarChart(medaeDataset, "MedAE Comparison", "Dataset", "MedAE");
        JFreeChart r2Chart = createBarChart(r2Dataset, "R² Comparison", "Dataset", "R²");

        // Create panels for each chart
        ChartPanel mseChartPanel = new ChartPanel(mseChart);
        ChartPanel maeChartPanel = new ChartPanel(maeChart);
        ChartPanel rmseChartPanel = new ChartPanel(rmseChart);
        ChartPanel mapeChartPanel = new ChartPanel(mapeChart);
        ChartPanel medaeChartPanel = new ChartPanel(medaeChart);
        ChartPanel r2ChartPanel = new ChartPanel(r2Chart);

        // Create a JFrame to display the comparison charts
        JFrame frame = new JFrame("Model Performance Comparison");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1500, 1000);
        frame.setLayout(new GridLayout(3, 2)); // 3 rows, 2 columns

        // Add charts to the frame
        frame.add(mseChartPanel);
        frame.add(maeChartPanel);
        frame.add(rmseChartPanel);
        frame.add(mapeChartPanel);
        frame.add(medaeChartPanel);
        frame.add(r2ChartPanel);

        // Display the frame
        frame.setVisible(true);
    }
}