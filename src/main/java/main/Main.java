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

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/main_scene.fxml"));

        // Set the scene
        Scene scene = new Scene(loader.load());

        // Configure the stage
        stage.setTitle("Car Analysis Application");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }

    static void processData() {
        // Initialize components for data processing
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
        String output = "C:\\Users\\LENOVO\\Desktop\\Junior\\project_oop_version2\\data\\Categorical_plots.png";
        analysis.analyzeCategoricalData(cleanedCarList, new String[]{"transmission", "fuelType", "typeCar"});
        System.out.println("\n--- Regression and Correlation Analysis ---");
        String outputPath = "C:\\Users\\LENOVO\\Desktop\\Junior\\project_oop_version2\\data\\scatter_plot_with_regression.png";
        visualizer.createScatterPlotsAndSave(cleanedCarList, attributes);

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
            Map<String, Object[][]> splitDataMap = preprocessor.splitData(preprocessor.getReducedData());

            // Retrieve individual splits
            Object[][] trainData = splitDataMap.get("train");
            Object[][] validationData = splitDataMap.get("validation");
            Object[][] testData = splitDataMap.get("test");

            // Log split sizes
            System.out.println("Training data size: " + trainData.length);
            System.out.println("Validation data size: " + validationData.length);
            System.out.println("Test data size: " + testData.length);

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

        Object[][] dataArray = new Object[cleanedCarList.size()][];  // Create 2D array for car data
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
