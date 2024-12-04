package main;

import model.Car;
import controller.DataUploadController;
import repository.CarRepository;
import utils.DataCleaningUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        DataUploadController controller = new DataUploadController();

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
    }
}
