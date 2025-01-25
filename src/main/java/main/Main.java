package main;

import model.Car;
import controller.DataUploadController;
import repository.CarRepository;
import service.DataAnalysis.DataAnalysisService;
import service.DataAnalysis.DataVisualizationAnalysisService;
import service.MachineLearning.*;
import utils.DataCleaningUtils;
import service.MachineLearning.MLModelTrainer;
import service.MachineLearning.EvaluationService;
import weka.classifiers.trees.RandomForest;

import java.util.List;
import java.util.Arrays;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        // Initialize components
        DataUploadController controller = new DataUploadController();
        DataAnalysisService analysis = new DataAnalysisService();
        DataVisualizationAnalysisService visualizer = new DataVisualizationAnalysisService(analysis);
        CarRepository carRepository = new CarRepository();
        List<List<String>> data;
        List<String> attributes = Arrays.asList("mileage", "year", "engineSize", "mpg");

        Object[][] trainFeatures = null;
        Object[][] trainTarget = null;
        Object[][] validationFeatures = null;
        Object[][] validationTarget = null;
        Object[][] testFeatures = null;
        Object[][] testTarget = null;

        // Loop for uploading data until successful
        while (true) {
            data = controller.getData();
            if (!data.isEmpty()) {
                System.out.println("Data successfully uploaded.");
                break;
            }
            try {
                Thread.sleep(500); // Pause before retry
            } catch (InterruptedException e) {
                System.err.println("Interrupted during sleep: " + e.getMessage());
            }
        }

        // Process raw data
        carRepository.processRawData(data);
        List<Car> carList = carRepository.getCars();
        List<Car> cleanedCarList = DataCleaningUtils.cleanCarData(carList, "N/A");

        // Perform data analysis
        System.out.println("\n--- Numerical Analysis ---");
        analysis.analyzeNumericalData(cleanedCarList, new String[]{"year", "price", "mileage", "roadTax", "mpg", "engineSize"});
        System.out.println("\n--- Categorical Analysis ---");
        analysis.analyzeCategoricalData(cleanedCarList, new String[]{"transmission", "fuelType", "typeCar"});
        System.out.println("\n--- Regression and Correlation Analysis ---");
        visualizer.createScatterPlotMatrix(cleanedCarList, attributes);

        // Convert cleaned data to 2D array
        Object[][] rawDataForPCA = convertCarListToDataArray(cleanedCarList);
        if (rawDataForPCA == null || rawDataForPCA.length == 0) {
            System.err.println("Error: Raw data for PCA is null or empty.");
            return;
        }

        System.out.println("Preparing data for preprocessing...");
        try {
            DataPreprocessor preprocessor = new DataPreprocessor(rawDataForPCA);
            double varianceThreshold = 0.95;

            // Preprocess and retrieve split data
            preprocessor.preprocessAndDisplayData(rawDataForPCA, varianceThreshold);
            Map<String, Object[][]> splitDataMap = preprocessor.splitData(preprocessor.getReducedData(), preprocessor.extractTargetVariable(preprocessor.getNormalizedData()));

            // Retrieve individual splits
            trainFeatures = splitDataMap.get("train_features");
            trainTarget = splitDataMap.get("train_target");
            validationFeatures = splitDataMap.get("validation_features");
            validationTarget = splitDataMap.get("validation_target");
            testFeatures = splitDataMap.get("test_features");
            testTarget = splitDataMap.get("test_target");

            // Log split sizes
            System.out.println("Training features size: " + trainFeatures.length);
            System.out.println("Training target size: " + trainTarget[0].length);
            System.out.println("Validation features size: " + validationFeatures.length);
            System.out.println("Validation target size: " + validationTarget[0].length);
            System.out.println("Test features size: " + testFeatures.length);
            System.out.println("Test target size: " + testTarget[0].length);

            System.out.println("trainFeatures rows: " + trainFeatures.length);
            System.out.println("trainFeatures columns: " + trainFeatures[0].length);
            System.out.println("trainTarget size: " + trainTarget.length);
            System.out.println("Data has been successfully reduced, split, and is ready for Machine Learning.");
        } catch (Exception e) {
            System.err.println("Error during data preprocessing: " + e.getMessage());
            e.printStackTrace();
        }

        // Train Machine Learning models
        try {
            System.out.println("\n--- Training Machine Learning Models ---");

            // Extract features and target from the training set
            double[][] trainFeatures_converted = convertToDoubleArray(trainFeatures);
            double[] trainTarget_converted = convertToDoubleColumn(trainTarget);
            double[][] validationFeatures_converted = convertToDoubleArray(validationFeatures);
            double[] validationTarget_converted = convertToDoubleColumn(validationTarget);
            double[][] testFeatures_converted = convertToDoubleArray(testFeatures);
            double[] testTarget_converted = convertToDoubleColumn(testTarget);
            /*
            System.out.println("First three values of trainTarget_converted data:");
            for (int i = 0; i < 3 && i < trainTarget_converted.length; i++) {
                System.out.println(Arrays.toString(new double[]{trainTarget_converted[i]}));
            }
            System.out.println("First three rows of trainFeatures_converted data:");
            for (int i = 0; i < 3 && i < trainFeatures_converted.length; i++) {
                System.out.println(Arrays.toString(trainFeatures_converted[i]));
            }*/

            // Train models
            MLModelTrainer modelTrainer = new MLModelTrainer();
            List<MLModelTrainer.ModelResult> results = modelTrainer.trainAndEvaluateModels(trainFeatures_converted, trainTarget_converted);

            // Visualize the results using a JFrame
            DataVisualizationMLService MLvisualizer = new DataVisualizationMLService();
            MLvisualizer.displayModelResultsInJFrame(results);

            // Initialize EvaluationService
            EvaluationService evaluationService = new EvaluationService();

            // Get the RandomForest model from the results
            RandomForest randomForestModel = (RandomForest) results.get(2).getModel();
            if (randomForestModel == null) {
                throw new IllegalStateException("RandomForest model is not properly trained.");
            }

            // Save the untuned model to a file
            evaluationService.saveModel(randomForestModel, "RandomForest.model");

            /*
            // Validate the model
            MLModelTrainer.ModelResult validationResult = evaluationService.validateModel(
                    (RandomForest) results.get(2).getModel(), validationFeatures_converted, validationTarget_converted
            );
            System.out.println("Validation Results:");
            System.out.println("MSE: " + validationResult.getMse());
            System.out.println("MAE: " + validationResult.getMae());
            System.out.println("RMSE: " + validationResult.getRmse());
            System.out.println("MAPE: " + validationResult.getMape());
            System.out.println("MedAE: " + validationResult.getMedae());
            System.out.println("R²: " + validationResult.getR2());

            // Test the model
            MLModelTrainer.ModelResult testResult = evaluationService.testModel(
                    (RandomForest) results.get(2).getModel(), testFeatures_converted, testTarget_converted
            );
            System.out.println("Test Results:");
            System.out.println("MSE: " + testResult.getMse());
            System.out.println("MAE: " + testResult.getMae());
            System.out.println("RMSE: " + testResult.getRmse());
            System.out.println("MAPE: " + testResult.getMape());
            System.out.println("MedAE: " + testResult.getMedae());
            System.out.println("R²: " + testResult.getR2());

            // Visualize evaluation and testing results
            DataVisualizationMLService visualizer1 = new DataVisualizationMLService();
            visualizer1.displayEvaluationAndTestResults(validationResult, testResult);

            // Perform feature selection
            double[][] reducedTrainFeatures = evaluationService.selectImportantFeatures(
                    randomForestModel, trainFeatures_converted, trainTarget_converted
            );
            double[][] reducedValidationFeatures = evaluationService.selectImportantFeatures(
                    randomForestModel, validationFeatures_converted, validationTarget_converted
            );
            double[][] reducedTestFeatures = evaluationService.selectImportantFeatures(
                    randomForestModel, testFeatures_converted, testTarget_converted
            );

            // Perform hyperparameter tuning on the reduced dataset
            RandomForest tunedModel = evaluationService.tuneHyperparameters(reducedTrainFeatures, trainTarget_converted);

            // Evaluate the tuned model
            MLModelTrainer.ModelResult tunedValidationResult = evaluationService.validateModel(
                    tunedModel, reducedValidationFeatures, validationTarget_converted
            );
            System.out.println("Tuned Validation Results:");
            System.out.println("MSE: " + tunedValidationResult.getMse());
            System.out.println("MAE: " + tunedValidationResult.getMae());
            System.out.println("RMSE: " + tunedValidationResult.getRmse());
            System.out.println("MAPE: " + tunedValidationResult.getMape());
            System.out.println("MedAE: " + tunedValidationResult.getMedae());
            System.out.println("R²: " + tunedValidationResult.getR2());

            MLModelTrainer.ModelResult tunedTestResult = evaluationService.testModel(
                    tunedModel, reducedTestFeatures, testTarget_converted
            );
            System.out.println("Tuned Test Results:");
            System.out.println("MSE: " + tunedTestResult.getMse());
            System.out.println("MAE: " + tunedTestResult.getMae());
            System.out.println("RMSE: " + tunedTestResult.getRmse());
            System.out.println("MAPE: " + tunedTestResult.getMape());
            System.out.println("MedAE: " + tunedTestResult.getMedae());
            System.out.println("R²: " + tunedTestResult.getR2());

            // Display comparison results in a new JFrame
            DataVisualizationMLService visualizer2 = new DataVisualizationMLService();
            visualizer2.displayComparisonResults(validationResult, testResult, tunedValidationResult, tunedTestResult);
            */
            /*
            // Load the trained model
            MLModel mlModel = new MLModel();
            mlModel.loadModel("randomForest_untuned.model");

            // Start the deployment server
            ModelDeploymentService server = new ModelDeploymentService(mlModel);
            server.startServer();*/


        } catch (Exception e) {
            System.err.println("Error during model training: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Helper method to convert List<Car> to a 2D array (Object[][]) format
    public static Object[][] convertCarListToDataArray(List<Car> cleanedCarList) {
        if (cleanedCarList == null || cleanedCarList.isEmpty()) {
            return null; // Return null if the list is empty or null
        }

        Object[][] dataArray = new Object[cleanedCarList.size()][];
        for (int i = 0; i < cleanedCarList.size(); i++) {
            Car car = cleanedCarList.get(i);
            if (car != null) {
                dataArray[i] = new Object[]{
                        car.getMileage(),
                        car.getYear(),
                        car.getEngineSize(),
                        car.getMpg(),
                        car.getPrice(),
                        car.getTransmission(),
                        car.getCarType(),
                        car.getModel(),
                        car.getFuelType()
                };
            } else {
                System.err.println("Warning: Null car object found at index " + i);
            }
        }
        return dataArray;
    }

    // Helper method to convert Object[][] to double[][]
    public static double[][] convertToDoubleArray(Object[][] data) {
        double[][] result = new double[data.length][data[0].length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                result[i][j] = ((Number) data[i][j]).doubleValue();
            }
        }
        return result;
    }

    // Helper method to convert Object[][] to double[]
    public static double[] convertToDoubleColumn(Object[][] data) {
        double[] result = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            try {
                if (data[i][0] instanceof Number) {
                    result[i] = ((Number) data[i][0]).doubleValue();
                } else if (data[i][0] instanceof String) {
                    result[i] = Double.parseDouble((String) data[i][0]);
                } else {
                    throw new IllegalArgumentException("Unsupported data type at row " + i + ": " + data[i][0]);
                }
            } catch (Exception e) {
                System.err.println("Error parsing value at row " + i + ": " + data[i][0]);
                throw e;
            }
        }
        return result;
    }
}