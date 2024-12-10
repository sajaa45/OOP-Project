package main;

import model.Car;
import controller.DataUploadController;
import repository.CarRepository;
import service.DataAnalysisService;
import service.DataVisualizationService;
import utils.DataCleaningUtils;

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
        Map<Integer, String> nColumnNames = Map.of(
                1, "Year",
                2, "Price",
                4, "Mileage",
                6, "Road Tax",
                7, "MPG",
                8, "Engine Size"
        );
        Map<Integer, String> cColumnNames = Map.of(
                0, "Model",
                3, "Transmission",
                5, "Fuel Type"
        );

        while (true) {
            data = controller.getData();
            if (!data.isEmpty()) {
                carRepository.processRawData(data);
                carRepository.processRawData(data);
                List<Car> carList = carRepository.getCars();
                table = carRepository.getData();
                Object[][] cleanedData = DataCleaningUtils.cleanData(table, "N/A");

                System.out.println("\n--- Numerical Analysis ---");
                analysis.analyzeNumericalData(cleanedData, new int[]{1, 2, 4, 6, 7, 8}, nColumnNames);

                System.out.println("\n--- Categorical Analysis ---");
                analysis.analyzeCategoricalData(cleanedData, new int[]{0, 3, 5}, cColumnNames);
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