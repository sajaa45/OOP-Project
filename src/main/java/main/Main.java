package main;
import utils.CSVMerger;
import utils.CSVDataInserter;
import java.util.List;
import service.DataAnalysisController;
import model.Car;


public class Main {
        public static void main(String[] args) {
                // Define file names and output file name
                String[] fileNames = {"data/audi.csv", "data/bmw.csv", "data/ford.csv", "data/hyundi.csv", "data/merc.csv", "data/skoda.csv", "data/toyota.csv"};
                String outputFileName = "data/merged_data.csv";

                // Merge CSV files into a single file
                CSVMerger csvMerger = new CSVMerger();
                csvMerger.mergeCSVFiles(fileNames, outputFileName);

                // Insert data into the database from the merged CSV file
                CSVDataInserter csvDataInserter = new CSVDataInserter();
                csvDataInserter.insertDataToDatabase(outputFileName);

                //Analyzing categorical and numerical features
                List<Car> cars = List.of(
                        new Car("Model S", 2020, 79999, 15000, "Electric", "Automatic", 0, 120, 2.0, "audi"),
                        new Car("Civic", 2019, 25000, 30000, "Petrol", "Manual", 150, 35, 1.6, "ford"),
                        new Car("Focus", 2018, 20000, 25000, "Diesel", "Manual", 200, 40, 1.8, "audi"),
                        new Car("Camry", 2021, 30000, 10000, "Hybrid", "Automatic", 100, 50, 2.5, "ford")
                );

                DataAnalysisController dataAnalysisController = new DataAnalysisController();

                System.out.println("Analyzing categorical feature: fuelType");
                dataAnalysisController.analyzeAndVisualizeCategoricalFeature(cars, "fuelType");

                System.out.println("Analyzing numerical feature: price");
                dataAnalysisController.analyzeAndVisualizeNumericalFeature(cars, "price");

        /*DataUploadController controller = new DataUploadController();

        while (true) {
            List<List<String>> data = controller.getData();
            if (!data.isEmpty()) {
                Object[][] rawDataArray = convertListTo2DArray(data);
                Object[][] cleanedData = DataCleaningUtils.cleanData(rawDataArray, "N/A");
                data = convert2DArrayToList(cleanedData);

                CarRepository carRepository = new CarRepository();
                carRepository.processRawData(data);

                System.out.println("Cars:");
                for (Car car : carRepository.getCars()) {
                    System.out.println(car);
                }

                System.out.println("\nTabular Data:");
                Object[][] table = carRepository.getData();
                for (Object[] row : table) {
                    for (Object cell : row) {
                        System.out.print(cell + "\t");
                    }
                    System.out.println();
                }
                break;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
        return Arrays.stream(array).map(row -> Arrays.stream(row).map(Object::toString).collect(Collectors.toList())).collect(Collectors.toList());
    }*/
        }}
