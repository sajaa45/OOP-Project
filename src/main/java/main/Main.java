package main;

import model.Car;
import controller.DataUploadController;
import repository.CarRepository;
import service.DataAnalysis.DataAnalysisService;
import service.DataAnalysis.DataVisualizationAnalysisService;
import service.MachineLearning.DataPreprocessor;
import utils.DataCleaningUtils;

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
            Object[][] trainFeatures = splitDataMap.get("train_features");
            Object[][] trainTarget = splitDataMap.get("train_target");
            Object[][] validationFeatures = splitDataMap.get("validation_features");
            Object[][] validationTarget = splitDataMap.get("validation_target");
            Object[][] testFeatures = splitDataMap.get("test_features");
            Object[][] testTarget = splitDataMap.get("test_target");

            // Log split sizes
            System.out.println("Training features size: " + trainFeatures.length);
            System.out.println("Training target size: " + trainTarget[0].length);
            System.out.println("Validation features size: " + validationFeatures.length);
            System.out.println("Validation target size: " + validationTarget[0].length);
            System.out.println("Test features size: " + testFeatures.length);
            System.out.println("Test target size: " + testTarget[0].length);

            // Ready for ML tasks
            System.out.println("Data has been successfully reduced, split, and is ready for Machine Learning.");
        } catch (Exception e) {
            System.err.println("Error during data preprocessing: " + e.getMessage());
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
}
