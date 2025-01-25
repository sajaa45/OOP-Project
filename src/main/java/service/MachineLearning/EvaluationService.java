package service.MachineLearning;

import java.util.ArrayList;
import java.util.Arrays;

import weka.classifiers.Classifier;
import weka.core.*;
import weka.classifiers.trees.RandomForest;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.CorrelationAttributeEval;
import weka.attributeSelection.Ranker;

public class EvaluationService {

    // Method to validate the model
    public MLModelTrainer.ModelResult validateModel(RandomForest model, double[][] validationFeatures, double[] validationTarget) {
        try {
            // Convert validation data to Weka Instances
            Instances validationDataset = createWekaDataset(validationFeatures, validationTarget);

            // Make predictions
            double[] predicted = predictWithModel(model, validationDataset);

            // Calculate metrics
            double mse = calculateMSEFromPredictions(validationTarget, predicted);
            double mae = calculateMAEFromPredictions(validationTarget, predicted);
            double rmse = calculateRMSEFromPredictions(validationTarget, predicted);
            double mape = calculateMAPEFromPredictions(validationTarget, predicted);
            double medae = calculateMedAEFromPredictions(validationTarget, predicted);
            double r2 = calculateRSquared(validationTarget, predicted);

            return new MLModelTrainer.ModelResult("Random Forest (Validation)", mse, mae, rmse, mape, medae, r2, model);
        } catch (Exception e) {
            e.printStackTrace();
            return new MLModelTrainer.ModelResult("Random Forest (Validation)", Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, null);
        }
    }

    // Method to test the model
    public MLModelTrainer.ModelResult testModel(RandomForest model, double[][] testFeatures, double[] testTarget) {
        try {
            // Convert test data to Weka Instances
            Instances testDataset = createWekaDataset(testFeatures, testTarget);

            // Make predictions
            double[] predicted = predictWithModel(model, testDataset);

            // Calculate metrics
            double mse = calculateMSEFromPredictions(testTarget, predicted);
            double mae = calculateMAEFromPredictions(testTarget, predicted);
            double rmse = calculateRMSEFromPredictions(testTarget, predicted);
            double mape = calculateMAPEFromPredictions(testTarget, predicted);
            double medae = calculateMedAEFromPredictions(testTarget, predicted);
            double r2 = calculateRSquared(testTarget, predicted);

            return new MLModelTrainer.ModelResult("Random Forest (Test)", mse, mae, rmse, mape, medae, r2, model);
        } catch (Exception e) {
            e.printStackTrace();
            return new MLModelTrainer.ModelResult("Random Forest (Test)", Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, null);
        }
    }

    // Method to calculate feature importances using CorrelationAttributeEval
    public double[] getFeatureImportances(Instances dataset) throws Exception {
        AttributeSelection attributeSelection = new AttributeSelection();
        CorrelationAttributeEval evaluator = new CorrelationAttributeEval(); // Use Correlation for numeric targets
        Ranker ranker = new Ranker();
        ranker.setNumToSelect(dataset.numAttributes() - 1); // Exclude the target attribute

        attributeSelection.setEvaluator(evaluator);
        attributeSelection.setSearch(ranker);
        attributeSelection.SelectAttributes(dataset);

        // Get the ranked attributes
        double[] importances = new double[dataset.numAttributes() - 1];
        int[] rankedAttributes = attributeSelection.selectedAttributes();

        for (int i = 0; i < rankedAttributes.length - 1; i++) { // Exclude the target attribute
            importances[rankedAttributes[i]] = evaluator.evaluateAttribute(rankedAttributes[i]);
        }

        return importances;
    }

    // Method to perform feature selection
    public double[][] selectImportantFeatures(RandomForest model, double[][] features, double[] target) {
        try {
            // Convert data to Weka Instances
            Instances dataset = createWekaDataset(features, target);

            // Get feature importances
            double[] importances = getFeatureImportances(dataset);

            // Identify top 20 features
            int[] topFeatureIndices = getTopFeatureIndices(importances, 20);

            // Select the top features
            double[][] reducedFeatures = new double[features.length][topFeatureIndices.length];
            for (int i = 0; i < features.length; i++) {
                for (int j = 0; j < topFeatureIndices.length; j++) {
                    reducedFeatures[i][j] = features[i][topFeatureIndices[j]];
                }
            }
            return reducedFeatures;
        } catch (Exception e) {
            e.printStackTrace();
            return features; // Fallback to original features if something goes wrong
        }
    }

    // Helper method to get the indices of the top N features
    private int[] getTopFeatureIndices(double[] importances, int topN) {
        int[] indices = new int[importances.length];
        for (int i = 0; i < indices.length; i++) {
            indices[i] = i;
        }

        // Sort indices based on importances
        for (int i = 0; i < importances.length; i++) {
            for (int j = i + 1; j < importances.length; j++) {
                if (importances[indices[i]] < importances[indices[j]]) {
                    int temp = indices[i];
                    indices[i] = indices[j];
                    indices[j] = temp;
                }
            }
        }

        // Return the top N indices
        return Arrays.copyOf(indices, topN);
    }

    // Helper method to tune hyperparameters
    public RandomForest tuneHyperparameters(double[][] features, double[] target) {
        try {
            Instances dataset = createWekaDataset(features, target);

            // Define hyperparameter grid
            String[] maxDepthOptions = {"10", "20", "20"};
            String[] minSamplesSplitOptions = {"2", "5", "10"};
            String[] nEstimatorsOptions = {"100", "200"};

            RandomForest bestModel = null;
            double bestMSE = Double.MAX_VALUE;

            // Perform grid search
            for (String maxDepth : maxDepthOptions) {
                for (String minSamplesSplit : minSamplesSplitOptions) {
                    for (String nEstimators : nEstimatorsOptions) {
                        RandomForest model = new RandomForest();
                        model.setOptions(new String[]{
                                "-I", nEstimators, // Number of trees
                                "-K", "0", // Use all features
                                "-depth", maxDepth // Maximum depth
                        });

                        // Train the model
                        model.buildClassifier(dataset);

                        // Evaluate model
                        double[] predicted = predictWithModel(model, dataset);
                        double mse = calculateMSEFromPredictions(target, predicted);

                        // Update best model
                        if (mse < bestMSE) {
                            bestMSE = mse;
                            bestModel = model;
                        }
                    }
                }
            }

            return bestModel;
        } catch (Exception e) {
            e.printStackTrace();
            return new RandomForest(); // Fallback to default model
        }
    }

    // Helper method to create a Weka dataset from features and target
    private Instances createWekaDataset(double[][] features, double[] target) {
        ArrayList<Attribute> attributes = new ArrayList<>();
        for (int i = 0; i < features[0].length; i++) {
            attributes.add(new Attribute("Feature" + i));
        }
        attributes.add(new Attribute("Target"));

        Instances dataset = new Instances("Dataset", attributes, features.length);
        dataset.setClassIndex(attributes.size() - 1);

        for (int i = 0; i < features.length; i++) {
            double[] instanceValues = new double[features[i].length + 1];
            System.arraycopy(features[i], 0, instanceValues, 0, features[i].length);
            instanceValues[features[i].length] = target[i];
            Instance instance = new DenseInstance(1.0, instanceValues);
            dataset.add(instance);
        }
        return dataset;
    }

    // Helper method to make predictions using a Weka model
    private double[] predictWithModel(Classifier model, Instances dataset) throws Exception {
        double[] predictions = new double[dataset.numInstances()];
        for (int i = 0; i < dataset.numInstances(); i++) {
            predictions[i] = model.classifyInstance(dataset.instance(i));
        }
        return predictions;
    }

    // Helper methods for calculating metrics
    private double calculateMSEFromPredictions(double[] actual, double[] predicted) {
        double sum = 0;
        for (int i = 0; i < actual.length; i++) {
            sum += Math.pow(actual[i] - predicted[i], 2);
        }
        return sum / actual.length;
    }

    private double calculateMAEFromPredictions(double[] actual, double[] predicted) {
        double sum = 0;
        for (int i = 0; i < actual.length; i++) {
            sum += Math.abs(actual[i] - predicted[i]);
        }
        return sum / actual.length;
    }

    private double calculateRMSEFromPredictions(double[] actual, double[] predicted) {
        return Math.sqrt(calculateMSEFromPredictions(actual, predicted));
    }

    private double calculateMAPEFromPredictions(double[] actual, double[] predicted) {
        double sum = 0;
        for (int i = 0; i < actual.length; i++) {
            sum += Math.abs((actual[i] - predicted[i]) / actual[i]);
        }
        return (sum / actual.length) * 100;
    }

    private double calculateMedAEFromPredictions(double[] actual, double[] predicted) {
        double[] residuals = new double[actual.length];
        for (int i = 0; i < actual.length; i++) {
            residuals[i] = Math.abs(actual[i] - predicted[i]);
        }
        Arrays.sort(residuals);
        if (residuals.length % 2 == 0) {
            return (residuals[residuals.length / 2] + residuals[residuals.length / 2 - 1]) / 2;
        } else {
            return residuals[residuals.length / 2];
        }
    }

    private double calculateRSquared(double[] actual, double[] predicted) {
        double meanActual = 0;
        for (double y : actual) {
            meanActual += y;
        }
        meanActual /= actual.length;

        double ssTotal = 0;
        double ssResidual = 0;
        for (int i = 0; i < actual.length; i++) {
            ssTotal += Math.pow(actual[i] - meanActual, 2);
            ssResidual += Math.pow(actual[i] - predicted[i], 2);
        }
        return 1 - (ssResidual / ssTotal);
    }

    // Method to save the model to a file
    public void saveModel(Classifier model, String filePath) {
        try {
            SerializationHelper.write(filePath, model);
            System.out.println("Model saved to: " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        EvaluationService evaluator = new EvaluationService();
        DataVisualizationMLService visualizer = new DataVisualizationMLService();

        // Sample test data
        double[][] features = {
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        };
        double[] target = {10.0, 20.0, 30.0};

        // Train a RandomForest model on the original dataset
        RandomForest originalModel = new RandomForest();
        try {
            Instances dataset = evaluator.createWekaDataset(features, target);
            originalModel.buildClassifier(dataset);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // Evaluate the original model
        MLModelTrainer.ModelResult originalValidationResult = evaluator.validateModel(originalModel, features, target);
        MLModelTrainer.ModelResult originalTestResult = evaluator.testModel(originalModel, features, target);

        // Display original model results in a JFrame
        visualizer.displayEvaluationAndTestResults(originalValidationResult, originalTestResult);

        // Perform feature selection
        double[][] reducedFeatures = evaluator.selectImportantFeatures(originalModel, features, target);

        // Perform hyperparameter tuning on the reduced dataset
        RandomForest tunedModel = evaluator.tuneHyperparameters(reducedFeatures, target);

        // Evaluate the tuned model
        MLModelTrainer.ModelResult tunedValidationResult = evaluator.validateModel(tunedModel, reducedFeatures, target);
        MLModelTrainer.ModelResult tunedTestResult = evaluator.testModel(tunedModel, reducedFeatures, target);

        // Display comparison results in a new JFrame
        visualizer.displayComparisonResults(originalValidationResult, originalTestResult, tunedValidationResult, tunedTestResult);
    }
}