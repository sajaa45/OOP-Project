package main;

import model.Car;
import controller.DataUploadController;
import repository.CarRepository;
import service.DataAnalysisService;
import service.DataVisualizationService;
import utils.DataCleaningUtils;

import java.util.List;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        DataUploadController controller = new DataUploadController();
        DataAnalysisService analysis = new DataAnalysisService();
        DataVisualizationService visualizer = new DataVisualizationService(analysis);
        CarRepository carRepository = new CarRepository();
        List<List<String>> data;
        List<String> attributes = Arrays.asList("mileage", "year", "engineSize", "mpg");

        while (true) {
            data = controller.getData();
            if (!data.isEmpty()) {
                carRepository.processRawData(data);
                List<Car> carList = carRepository.getCars();
                List<Car> cleanedCarList = DataCleaningUtils.cleanCarData(carList, "N/A");

                System.out.println("\n--- Numerical Analysis ---");
                analysis.analyzeNumericalData(cleanedCarList, new String[]{"year", "price", "mileage", "roadTax", "mpg", "engineSize"} );
                System.out.println("\n--- Categorical Analysis ---");
                analysis.analyzeCategoricalData(cleanedCarList, new String[]{"transmission", "fuelType", "typeCar"} );
                System.out.println("\n--- Regression and Correlation Analysis ---");
                visualizer.createScatterPlotMatrix(cleanedCarList, attributes);
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