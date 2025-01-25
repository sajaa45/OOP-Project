/*package service.MachineLearning;

import weka.classifiers.Classifier;
import weka.core.SerializationHelper;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.DenseInstance;
import weka.core.Attribute;
import java.util.ArrayList;

public class MLModel {
    private Classifier model;

    // Load the model from a file
    public void loadModel(String filePath) {
        try {
            model = (Classifier) SerializationHelper.read(filePath);
            System.out.println("Model loaded successfully from: " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Make a prediction using the loaded model
    public double predict(double[] features) {
        try {
            // Convert features to a Weka Instance
            Instances dataset = createWekaInstance(features);
            dataset.setClassIndex(dataset.numAttributes() - 1); // Set the target attribute

            // Make a prediction
            return model.classifyInstance(dataset.firstInstance());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to make prediction.");
        }
    }

    // Create a Weka Instance from input features
    private Instances createWekaInstance(double[] features) {
        ArrayList<Attribute> attributes = new ArrayList<>();
        for (int i = 0; i < features.length; i++) {
            attributes.add(new Attribute("Feature" + i));
        }
        Instances dataset = new Instances("PredictionInstance", attributes, 0);
        dataset.add(new DenseInstance(1.0, features));
        return dataset;
    }
}*/