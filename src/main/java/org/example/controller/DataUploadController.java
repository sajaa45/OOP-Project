package org.example.controller;

import org.example.Service.DataIngestionService;
import java.util.List;

public class DataUploadController {
    private DataIngestionService dataIngestionService;

    public DataUploadController() {
        this.dataIngestionService = new DataIngestionService();
    }

    public void uploadCSV(String filePath, List<List<String>> csvData) {
        dataIngestionService.loadDataFromCSV(filePath, csvData);
    }

    public void uploadDatabaseData(String dbUrl, String dbUsername, String dbPassword, String query, List<List<String>> dbData) {
        dataIngestionService.loadDataFromDatabase(dbUrl, dbUsername, dbPassword, query, dbData);
    }
}
