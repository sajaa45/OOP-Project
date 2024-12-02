package org.example.Main;

import org.example.Model.Car;
import org.example.controller.DataUploadController;
import org.example.Repository.CarRepository;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        DataUploadController controller = new DataUploadController();

        while (true) {
            List<List<String>> data = controller.getData();
            if (!data.isEmpty()) {
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
//data cleaning (gisele)
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }}
