package service.MachineLearning;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import weka.classifiers.trees.REPTree;
import weka.classifiers.trees.RandomForest;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Attribute;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

public class MLModelTrainer {

    // Inner class to hold model results
    public static class ModelResult {
        private final String modelName;
        private final double mse; // Mean Squared Error
        private final double mae; // Mean Absolute Error
        private final double rmse; // Root Mean Squared Error
        private final double mape; // Mean Absolute Percentage Error
        private final double medae; // Median Absolute Error
        private final double r2;  // R-squared
        private final Object model; // The trained model

        public ModelResult(String modelName, double mse, double mae,double rmse, double mape, double medae, double r2, Object model) {
            this.modelName = modelName;
            this.mse = mse;
            this.mae = mae;
            this.rmse = rmse;
            this.mape = mape;
            this.medae = medae;
            this.r2 = r2;
            this.model = model;
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

        public double getRmse() {
            return rmse;
        }

        public double getMape() {
            return mape;
        }

        public double getMedae() {
            return medae;
        }

        public double getR2() {
            return r2;
        }

        public Object getModel() {
            return model;
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

        // Train and evaluate SVR
        results.add(trainSVR(features, target));

        for (ModelResult result : results) {
            System.out.println(result.getModelName() + ":");
            System.out.println("MSE: " + result.getMse());
            System.out.println("MAE: " + result.getMae());
            System.out.println("RMSE: " + result.getRmse());
            System.out.println("MAPE: " + result.getMape());
            System.out.println("MedAE: " + result.getMedae());
            System.out.println("R²: " + result.getR2());
            System.out.println();
        }

        return results;
    }

    private ModelResult trainLinearRegression(double[][] features, double[] target) {
        OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
        regression.newSampleData(target, features);

        double[] coefficients = regression.estimateRegressionParameters();

        double[] predicted = new double[target.length];
        for (int i = 0; i < features.length; i++) {
            double prediction = coefficients[0]; // Start with the intercept
            for (int j = 0; j < features[i].length; j++) {
                prediction += coefficients[j + 1] * features[i][j]; // Add slope * feature value
            }
            predicted[i] = prediction;
        }

        // Calculate residuals manually
        double[] residuals = new double[target.length];
        for (int i = 0; i < target.length; i++) {
            residuals[i] = target[i] - predicted[i];
        }
        double mse = calculateMSE(residuals);
        double mae = calculateMAE(residuals);
        double rmse = calculateRMSE(residuals);
        double mape = calculateMAPE(target, residuals);
        double medae = calculateMedAE(residuals);
        double r2 = regression.calculateAdjustedRSquared();

        return new ModelResult("Linear Regression", mse, mae, rmse, mape, medae, r2, regression);
    }

    private ModelResult trainDecisionTree(double[][] features, double[] target) {
        REPTree tree = new REPTree();
        Instances dataset = createWekaDataset(features, target);

        try {
            tree.buildClassifier(dataset);
            double[] predicted = predictWithModel(tree, dataset);
            double mse = calculateMSEFromPredictions(target, predicted);
            double mae = calculateMAEFromPredictions(target, predicted);
            double rmse = calculateRMSEFromPredictions(target, predicted);
            double mape = calculateMAPEFromPredictions(target, predicted);
            double medae = calculateMedAEFromPredictions(target, predicted);
            double r2 = calculateRSquared(target, predicted);

            return new ModelResult("Decision Tree", mse, mae, rmse, mape, medae, r2, tree);
        } catch (Exception e) {
            e.printStackTrace();
            return new ModelResult("Decision Tree", Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, null);
        }
    }

    private ModelResult trainRandomForest(double[][] features, double[] target) {
        RandomForest forest = new RandomForest();
        Instances dataset = createWekaDataset(features, target);

        try {
            forest.buildClassifier(dataset);
            double[] predicted = predictWithModel(forest, dataset);
            double mse = calculateMSEFromPredictions(target, predicted);
            double mae = calculateMAEFromPredictions(target, predicted);
            double rmse = calculateRMSEFromPredictions(target, predicted);
            double mape = calculateMAPEFromPredictions(target, predicted);
            double medae = calculateMedAEFromPredictions(target, predicted);
            double r2 = calculateRSquared(target, predicted);

            return new ModelResult("Random Forest", mse, mae, rmse, mape, medae, r2, forest);
        } catch (Exception e) {
            e.printStackTrace();
            return new ModelResult("Random Forest", Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, null);
        }
    }


    private ModelResult trainSVR(double[][] features, double[] target) {
        try {
            svm_problem prob = new svm_problem();
            prob.l = target.length;
            prob.x = new svm_node[prob.l][features[0].length];
            prob.y = new double[prob.l];

            for (int i = 0; i < prob.l; i++) {
                prob.x[i] = new svm_node[features[i].length];
                for (int j = 0; j < features[i].length; j++) {
                    prob.x[i][j] = new svm_node();
                    prob.x[i][j].index = j + 1;
                    prob.x[i][j].value = features[i][j];
                }
                prob.y[i] = target[i];
            }

            // Set SVR parameters
            svm_parameter param = new svm_parameter();
            param.svm_type = svm_parameter.EPSILON_SVR;
            param.kernel_type = svm_parameter.RBF;
            param.C = 1.0;
            param.eps = 0.1;
            param.gamma = 0.1;

            // Train SVR model
            svm_model model = svm.svm_train(prob, param);

            // Make predictions
            double[] predicted = new double[target.length];
            for (int i = 0; i < target.length; i++) {
                predicted[i] = svm.svm_predict(model, prob.x[i]);
            }

            // Calculate metrics
            double mse = calculateMSEFromPredictions(target, predicted);
            double mae = calculateMAEFromPredictions(target, predicted);
            double rmse = calculateRMSEFromPredictions(target, predicted);
            double mape = calculateMAPEFromPredictions(target, predicted);
            double medae = calculateMedAEFromPredictions(target, predicted);
            double r2 = calculateRSquared(target, predicted);

            return new ModelResult("SVR", mse, mae, rmse, mape, medae, r2, model);
        } catch (Exception e) {
            e.printStackTrace();
            return new ModelResult("SVR", Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, null);
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

    private double[] predictWithModel(weka.classifiers.Classifier model, Instances dataset) throws Exception {
        double[] predictions = new double[dataset.numInstances()];
        for (int i = 0; i < dataset.numInstances(); i++) {
            predictions[i] = model.classifyInstance(dataset.instance(i));
        }
        return predictions;
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

    private double calculateRMSE(double[] residuals) {
        double sum = 0;
        for (double r : residuals) {
            sum += r * r;
        }
        return Math.sqrt(sum / residuals.length);
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

    private double calculateMAPE(double[] actual, double[] predicted) {
        double sum = 0;
        int count = 0;
        for (int i = 0; i < actual.length; i++) {
            if (actual[i] != 0) { // Skip zero values
                sum += Math.abs((actual[i] - predicted[i]) / actual[i]);
                count++;
            }
        }
        return (sum / count) * 100;
    }

    private double calculateMedAE(double[] residuals) {
        Arrays.sort(residuals);
        if (residuals.length % 2 == 0) {
            return (residuals[residuals.length / 2] + residuals[residuals.length / 2 - 1]) / 2;
        } else {
            return residuals[residuals.length / 2];
        }
    }

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
        return calculateMedAE(residuals);
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

        // Debug: Print metrics to console
        for (ModelResult result : results) {
            System.out.println(result.getModelName() + ":");
            System.out.println("MSE: " + result.getMse());
            System.out.println("MAE: " + result.getMae());
            System.out.println("RMSE: " + result.getRmse());
            System.out.println("MAPE: " + result.getMape());
            System.out.println("MedAE: " + result.getMedae());
            System.out.println("R²: " + result.getR2());
            System.out.println();
        }

        // Display results in a styled JFrame
        JFrame frame = new JFrame("Model Results");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1500, 400); // Increased width
        frame.setLayout(new BorderLayout());

        JLabel headerLabel = new JLabel("Machine Learning Model Results", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        frame.add(headerLabel, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(results.size() + 1, 7, 10, 10)); // 7 columns
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add headers with styling
        JLabel modelNameHeader = new JLabel("Model Name", JLabel.CENTER);
        JLabel mseHeader = new JLabel("MSE", JLabel.CENTER);
        JLabel maeHeader = new JLabel("MAE", JLabel.CENTER);
        JLabel rmseHeader = new JLabel("RMSE", JLabel.CENTER);
        JLabel mapeHeader = new JLabel("MAPE", JLabel.CENTER);
        JLabel medaeHeader = new JLabel("MedAE", JLabel.CENTER);
        JLabel r2Header = new JLabel("R-squared", JLabel.CENTER);

        // Set preferred size for headers
        int labelWidth = 150;
        int labelHeight = 30;
        modelNameHeader.setPreferredSize(new Dimension(labelWidth, labelHeight));
        mseHeader.setPreferredSize(new Dimension(labelWidth, labelHeight));
        maeHeader.setPreferredSize(new Dimension(labelWidth, labelHeight));
        rmseHeader.setPreferredSize(new Dimension(labelWidth, labelHeight));
        mapeHeader.setPreferredSize(new Dimension(labelWidth, labelHeight));
        medaeHeader.setPreferredSize(new Dimension(labelWidth, labelHeight));
        r2Header.setPreferredSize(new Dimension(labelWidth, labelHeight));

        modelNameHeader.setFont(new Font("Arial", Font.BOLD, 16));
        mseHeader.setFont(new Font("Arial", Font.BOLD, 16));
        maeHeader.setFont(new Font("Arial", Font.BOLD, 16));
        rmseHeader.setFont(new Font("Arial", Font.BOLD, 16));
        mapeHeader.setFont(new Font("Arial", Font.BOLD, 16));
        medaeHeader.setFont(new Font("Arial", Font.BOLD, 16));
        r2Header.setFont(new Font("Arial", Font.BOLD, 16));

        panel.add(modelNameHeader);
        panel.add(mseHeader);
        panel.add(maeHeader);
        panel.add(rmseHeader);
        panel.add(mapeHeader);
        panel.add(medaeHeader);
        panel.add(r2Header);

        // Add results with styling
        for (ModelResult result : results) {
            JLabel modelName = new JLabel(result.getModelName(), JLabel.CENTER);
            JLabel mse = new JLabel(String.format("%.2f", result.getMse()), JLabel.CENTER);
            JLabel mae = new JLabel(String.format("%.2f", result.getMae()), JLabel.CENTER);
            JLabel rmse = new JLabel(String.format("%.2f", result.getRmse()), JLabel.CENTER);
            JLabel mape = new JLabel(String.format("%.2f", result.getMape()), JLabel.CENTER);
            JLabel medae = new JLabel(String.format("%.2f", result.getMedae()), JLabel.CENTER);
            JLabel r2 = new JLabel(String.format("%.2f", result.getR2()), JLabel.CENTER);

            modelName.setPreferredSize(new Dimension(labelWidth, labelHeight));
            mse.setPreferredSize(new Dimension(labelWidth, labelHeight));
            mae.setPreferredSize(new Dimension(labelWidth, labelHeight));
            rmse.setPreferredSize(new Dimension(labelWidth, labelHeight));
            mape.setPreferredSize(new Dimension(labelWidth, labelHeight));
            medae.setPreferredSize(new Dimension(labelWidth, labelHeight));
            r2.setPreferredSize(new Dimension(labelWidth, labelHeight));

            modelName.setFont(new Font("Arial", Font.PLAIN, 14));
            mse.setFont(new Font("Arial", Font.PLAIN, 14));
            mae.setFont(new Font("Arial", Font.PLAIN, 14));
            rmse.setFont(new Font("Arial", Font.PLAIN, 14));
            mape.setFont(new Font("Arial", Font.PLAIN, 14));
            medae.setFont(new Font("Arial", Font.PLAIN, 14));
            r2.setFont(new Font("Arial", Font.PLAIN, 14));

            panel.add(modelName);
            panel.add(mse);
            panel.add(mae);
            panel.add(rmse);
            panel.add(mape);
            panel.add(medae);
            panel.add(r2);
        }

        // Add scroll pane
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
   }
}
