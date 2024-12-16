package main;

import model.Car;
import controller.DataUploadController;
import repository.CarRepository;
import service.DataAnalysisService;
import service.DataVisualizationService;
import utils.DataCleaningUtils;

import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        DataUploadController controller = new DataUploadController();
        List<List<String>> data;

        while (true) {
            data = controller.getData();
            if (!data.isEmpty()) {
                processData(data);
                break; // Exit the loop after processing
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    private static void processData(List<List<String>> data) {
        // Step 1: Data Cleaning
        Object[][] rawDataArray = convertListTo2DArray(data);
        Object[][] cleanedData = DataCleaningUtils.cleanData(rawDataArray, "N/A");
        data = convert2DArrayToList(cleanedData);

        // Step 2: Data Processing and Repository Setup
        CarRepository carRepository = new CarRepository();
        carRepository.processRawData(data);

        // Get the list of Car objects from the repository
        List<Car> carList = carRepository.getCars();

        // Step 3: Initialize Analysis and Visualization Services
        DataAnalysisService analysis = new DataAnalysisService(carList);
        DataVisualizationService visualizer = new DataVisualizationService(analysis);

        // Step 5: Generate Scatter Plot Matrix for Selected Attributes
        List<String> attributes = Arrays.asList("price", "mileage", "year", "engineSize", "mpg");
        visualizer.createScatterPlotMatrix(carList, attributes);
    }

    private static Object[][] convertListTo2DArray(List<List<String>> data) {
        int rows = data.size();
        int cols = data.get(0).size();
        Object[][] array = new Object[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                array[i][j] = data.get(i).get(j);
            }
        }
        return array;
    }

    private static List<List<String>> convert2DArrayToList(Object[][] array) {
        return Arrays.stream(array)
                .map(row -> Arrays.stream(row).map(Object::toString).collect(Collectors.toList()))
                .collect(Collectors.toList());
    }
}
