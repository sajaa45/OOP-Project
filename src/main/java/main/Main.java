package main;

import controller.DataUploadController;
import repository.CarRepository;
import utils.DataCleaningUtils;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        DataUploadController controller = new DataUploadController();
        Object[][] table = null;

        while (true) {
            List<List<String>> data = controller.getData();
            if (!data.isEmpty()) {
                CarRepository carRepository = new CarRepository();
                carRepository.processRawData(data);

                table = carRepository.getData();
                Object[][] cleanedData = DataCleaningUtils.cleanData(table, "N/A");

                System.out.println("\nCleaned Data:");
                for (Object[] row : cleanedData) {
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
    }}