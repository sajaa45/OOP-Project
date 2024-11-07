package org.example.Main;

import org.example.controller.DataUploadController;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Initialize the controller
        DataUploadController dataUploadController = new DataUploadController();

        // List to store data from CSV
        List<List<String>> csvData = new ArrayList<>();
        String csvFilePath = "C:\\Users\\LENOVO\\Downloads\\archive(1)\\bmw.csv"; // Path to your CSV file

        // Load data from CSV
        dataUploadController.uploadCSV(csvFilePath, csvData);
        System.out.println("CSV Data:");
        for (List<String> row : csvData) {
            System.out.println(row);
        }

        // List to store data from the database
        List<List<String>> dbData = new ArrayList<>();
        String dbUrl = "jdbc:mysql://localhost:3306/car"; // Replace with your DB URL
        String dbUsername = "root"; // Replace with your DB username
        String dbPassword = "root"; // Replace with your DB password
        String query = "SELECT * FROM bmw limit 4"; // Replace with your table name

        // Load data from Database
        dataUploadController.uploadDatabaseData(dbUrl, dbUsername, dbPassword, query, dbData);
        System.out.println("\nDatabase Data:");
        for (List<String> row : dbData) {
            System.out.println(row);
        }
    }
}
