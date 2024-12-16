package main;

import model.Car;
import controller.DataUploadController;
import repository.CarRepository;
import service.DataAnalysisService;
import service.DataVisualizationService;
import utils.DataCleaningUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Arrays;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        DataUploadController controller = new DataUploadController();
        DataAnalysisService analysis = new DataAnalysisService();
        DataVisualizationService visualizer = new DataVisualizationService(analysis);
        CarRepository carRepository = new CarRepository();

        List<List<String>> data;
        List<String> attributes = Arrays.asList("price", "mileage", "year", "engineSize", "mpg");
        Object[][] table = null;

        Map<Integer, String> columnNames = new HashMap<>();
        columnNames.put(0, "Model");
        columnNames.put(1, "Year");
        columnNames.put(2, "Price");
        columnNames.put(3, "Transmission");
        columnNames.put(4, "Mileage");
        columnNames.put(5, "Fuel Type");
        columnNames.put(6, "Road Tax");
        columnNames.put(7, "MPG");
        columnNames.put(8, "Engine Size");

        while (true) {
            data = controller.getData();
            if (!data.isEmpty()) {
                carRepository.processRawData(data);
                carRepository.processRawData(data);
                List<Car> carList = carRepository.getCars();
                table = carRepository.getData();
                Object[][] cleanedData = DataCleaningUtils.cleanData(table, "N/A");

                System.out.println("\n--- Numerical Analysis ---");
                analysis.analyzeNumericalData(table, new int[]{1, 2, 4, 6, 7, 8}, columnNames);
                System.out.println("\n--- Categorical Analysis ---");
                analysis.analyzeCategoricalData(table, new int[]{0, 3, 5}, columnNames);
                visualizer.createScatterPlotMatrix(carList, attributes);
                break;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



}