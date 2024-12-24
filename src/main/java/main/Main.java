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

public class Main {

    public static void main(String[] args) {
        DataUploadController controller = new DataUploadController();
        DataAnalysisService analysis = new DataAnalysisService();
        DataVisualizationAnalysisService visualizer = new DataVisualizationAnalysisService(analysis);
        CarRepository carRepository = new CarRepository();
        List<List<String>> data;
        List<String> attributes = Arrays.asList("mileage", "year", "engineSize", "mpg");
        List<Car> cleanedCarList = null; // newly added

        while (true) {
            data = controller.getData();
            if (data == null || data.isEmpty()) {
                System.out.println("Data is null or empty, retrying...");
                continue; // Skip this iteration of the loop and try again
            }
            carRepository.processRawData(data);
            List<Car> carList = carRepository.getCars();
            cleanedCarList = DataCleaningUtils.cleanCarData(carList, "N/A");

            // Validate cleanedCarList
            if (cleanedCarList == null || cleanedCarList.isEmpty()) {
                System.err.println("Error: cleanedCarList is null or empty.");
                return; // Exit if the list is invalid
            }

            for (Car car : cleanedCarList) {
                if (car == null) {
                    System.err.println("Error: Null car object found in cleanedCarList.");
                    return; // Exit if any car object is null
                }
            }

            System.out.println("\n--- Numerical Analysis ---");
            analysis.analyzeNumericalData(cleanedCarList, new String[]{"year", "price", "mileage", "roadTax", "mpg", "engineSize"});
            System.out.println("\n--- Categorical Analysis ---");
            analysis.analyzeCategoricalData(cleanedCarList, new String[]{"transmission", "fuelType", "typeCar"});
            System.out.println("\n--- Regression and Correlation Analysis ---");
            visualizer.createScatterPlotMatrix(cleanedCarList, attributes);
            break;
        }

        // Get cleaned data and convert to a 2D array
        Object[][] rawDataForPCA = convertCarListToDataArray(cleanedCarList);

        // Check if rawDataForPCA is null or empty
        if (rawDataForPCA == null || rawDataForPCA.length == 0) {
            System.err.println("Error: rawDataForPCA is null or empty.");
            return; // Exit if raw data is invalid
        } else {
            System.out.println("Converted car list to data array with " + rawDataForPCA.length + " rows.");
            System.out.println("Data successfully converted for preprocessing.");
        }

        // Initialize DataPreprocessor
        if (rawDataForPCA == null || rawDataForPCA.length == 0) {
            System.err.println("Error: rawDataForPCA is null or empty.");
            return; // Exit if raw data is invalid
        } else {
            try {
                DataPreprocessor preprocessor = new DataPreprocessor(rawDataForPCA);
                double varianceThreshold = 0.95;
                preprocessor.preprocessAndDisplayData(rawDataForPCA, varianceThreshold);
            } catch (Exception e) {
                System.err.println("Error during data preprocessing: " + e.getMessage());
                e.printStackTrace();
                return; // Exit if preprocessing fails
            }
        }

    }

    // Helper method to convert List<Car> to a 2D array (Object[][]) format
    public static Object[][] convertCarListToDataArray(List<Car> cleanedCarList) {
        if (cleanedCarList == null || cleanedCarList.isEmpty()) {
            return null;  // Return null if the list is empty or null
        }

        Object[][] dataArray = new Object[cleanedCarList.size()][];

        for (int i = 0; i < cleanedCarList.size(); i++) {
            Car car = cleanedCarList.get(i);
            if (car != null) {

                dataArray[i] = new Object[] {
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
                System.err.println("Error: Null car object found at index " + i);
            }
        }
        /*
        // Check if dataArray is populated correctly
        if (dataArray.length == 0) {
            System.err.println("Error: dataArray is empty.");
        }
        return dataArray;
        */
        // Print the first 20 rows of the dataArray
        System.out.println("dataArray content:");
        for (int i = 0; i < Math.min(dataArray.length, 20); i++) {
            System.out.println(Arrays.toString(dataArray[i]));
        }

        // Check if dataArray is populated correctly
        if (dataArray.length == 0) {
            System.err.println("Error: dataArray is empty.");
        }
        return dataArray;
    }

}
