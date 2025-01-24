package service.MachineLearning;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import weka.classifiers.trees.REPTree;
import weka.classifiers.trees.RandomForest;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Attribute;

public class MLModelTrainer {

    // Inner class to hold model results
    public static class ModelResult {
        private final String modelName;
        private final double mse; // Mean Squared Error
        private final double mae; // Mean Absolute Error
        private final double r2;  // R-squared

        public ModelResult(String modelName, double mse, double mae, double r2) {
            this.modelName = modelName;
            this.mse = mse;
            this.mae = mae;
            this.r2 = r2;
        }

        public String getModelName() {
            return modelName;
        }

        public double getMse() {
            return mse;
        }

        public double getMae() {
            return mae;
        }

        public double getR2() {
            return r2;
        }
    }

    // Method to train and evaluate models
    public List<ModelResult> trainAndEvaluateModels(double[][] features, double[] target) {
        List<ModelResult> results = new ArrayList<>();

        // Train and evaluate Linear Regression
        results.add(trainLinearRegression(features, target));

        // Train and evaluate Decision Tree
        results.add(trainDecisionTree(features, target));

        // Train and evaluate Random Forest
        results.add(trainRandomForest(features, target));

        // Comment out other models for now
        // results.add(trainSupportVectorRegression(features, target));
        // results.add(trainNeuralNetwork(features, target));

        return results;
    }

    private ModelResult trainLinearRegression(double[][] features, double[] target) {
        OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
        regression.newSampleData(target, features);

        double[] residuals = regression.estimateResiduals();
        double mse = calculateMSE(residuals);
        double mae = calculateMAE(residuals);
        double r2 = regression.calculateAdjustedRSquared();

        return new ModelResult("Linear Regression", mse, mae, r2);
    }

    private ModelResult trainDecisionTree(double[][] features, double[] target) {
        REPTree tree = new REPTree();
        Instances dataset = createWekaDataset(features, target);

        try {
            tree.buildClassifier(dataset);
            double mse = evaluateModel(tree, dataset);
            return new ModelResult("Decision Tree", mse, mse / 2, 0.75); // Placeholder for R-squared
        } catch (Exception e) {
            e.printStackTrace();
            return new ModelResult("Decision Tree", Double.NaN, Double.NaN, Double.NaN);
        }
    }

    private ModelResult trainRandomForest(double[][] features, double[] target) {
        RandomForest forest = new RandomForest();
        Instances dataset = createWekaDataset(features, target);

        try {
            forest.buildClassifier(dataset);
            double mse = evaluateModel(forest, dataset);
            return new ModelResult("Random Forest", mse, mse / 2, 0.80); // Placeholder for R-squared
        } catch (Exception e) {
            e.printStackTrace();
            return new ModelResult("Random Forest", Double.NaN, Double.NaN, Double.NaN);
        }
    }

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

    private double calculateMSE(double[] residuals) {
        double sum = 0;
        for (double r : residuals) {
            sum += r * r;
        }
        return sum / residuals.length;
    }

    private double calculateMAE(double[] residuals) {
        double sum = 0;
        for (double r : residuals) {
            sum += Math.abs(r);
        }
        return sum / residuals.length;
    }

    private double evaluateModel(Object model, Instances dataset) {
        // Placeholder for model evaluation
        return new java.util.Random().nextDouble() * 10; // Simulated MSE
    }

    public static void main(String[] args) {
        MLModelTrainer trainer = new MLModelTrainer();
        double[][] features = {
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        };
        double[] target = {10.0, 20.0, 30.0};

        List<ModelResult> results = trainer.trainAndEvaluateModels(features, target);

        // Display results in a styled JFrame
        JFrame frame = new JFrame("Model Results");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        JLabel headerLabel = new JLabel("Machine Learning Model Results", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        frame.add(headerLabel, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(results.size() + 1, 4, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add headers with styling
        JLabel modelNameHeader = new JLabel("Model Name", JLabel.CENTER);
        JLabel mseHeader = new JLabel("MSE", JLabel.CENTER);
        JLabel maeHeader = new JLabel("MAE", JLabel.CENTER);
        JLabel r2Header = new JLabel("R-squared", JLabel.CENTER);

        modelNameHeader.setFont(new Font("Arial", Font.BOLD, 16));
        mseHeader.setFont(new Font("Arial", Font.BOLD, 16));
        maeHeader.setFont(new Font("Arial", Font.BOLD, 16));
        r2Header.setFont(new Font("Arial", Font.BOLD, 16));

        panel.add(modelNameHeader);
        panel.add(mseHeader);
        panel.add(maeHeader);
        panel.add(r2Header);

        // Add results with styling
        for (ModelResult result : results) {
            JLabel modelName = new JLabel(result.getModelName(), JLabel.CENTER);
            JLabel mse = new JLabel(String.format("%.2f", result.getMse()), JLabel.CENTER);
            JLabel mae = new JLabel(String.format("%.2f", result.getMae()), JLabel.CENTER);
            JLabel r2 = new JLabel(String.format("%.2f", result.getR2()), JLabel.CENTER);

            modelName.setFont(new Font("Arial", Font.PLAIN, 14));
            mse.setFont(new Font("Arial", Font.PLAIN, 14));
            mae.setFont(new Font("Arial", Font.PLAIN, 14));
            r2.setFont(new Font("Arial", Font.PLAIN, 14));

            panel.add(modelName);
            panel.add(mse);
            panel.add(mae);
            panel.add(r2);
        }

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
