package org.example.Service;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataIngestionService{
    public static void loadDataFromCSV(String filePath, List<List<String>> csvData ){
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split by comma and store each cell in a list
                String[] values = line.split(",");
                List<String> row = new ArrayList<>();
                for (String value : values) {
                    row.add(value.trim()); // Trim to clean up whitespace
                }
                csvData.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadDataFromDatabase(String dbUrl, String dbUsername, String dbPassword, String query, List<List<String>> data) {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            int columnCount = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                List<String> row = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(rs.getString(i));
                }
                data.add(row);
            }
        } catch (SQLException e) {
            System.out.println("Failed to load data from the database.");
            e.printStackTrace();
        }
    }

    public void loadDataFromExcel(String filePath, List<List<String>> data) {

        try (FileInputStream fis = new FileInputStream(filePath);

             Workbook workbook = filePath.endsWith(".xls") ? new HSSFWorkbook(fis) : new XSSFWorkbook(fis)) {


            Sheet sheet = workbook.getSheetAt(0); // Read the first sheet

            for (Row row : sheet) {

                List<String> rowData = new ArrayList<>();

                for (Cell cell : row) {

                    rowData.add(cell.toString()); // Add cell data to rowData

                }

                data.add(rowData); // Add rowData to the main data list

            }

        } catch (IOException e) {

            e.printStackTrace();

        }

}}
