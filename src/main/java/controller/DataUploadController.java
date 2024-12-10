package controller;

import service.DataIngestionService;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DataUploadController {

    private JFrame frame;
    private JTextArea textArea;
    private DataIngestionService dataIngestionService;
    private List<List<String>> data = new ArrayList<>();
    public DataUploadController() {
        this.data = new ArrayList<>();
        initializeUI();
    }
    // Process the selected file and return the data
    private void processFile(File file) {
        try {
            if (file.getName().endsWith(".csv")) {
                data = DataIngestionService.loadDataFromCSV(file.getAbsolutePath());
            } else if (file.getName().endsWith(".xls") || file.getName().endsWith(".xlsx")) {
                data = DataIngestionService.loadDataFromExcel(file.getAbsolutePath());
            } else {
                throw new IllegalArgumentException("Unsupported file format");
            }

            displayData(); // Show data in the text area
        } catch (Exception e) {
            e.printStackTrace();
            textArea.setText("Error processing file: " + e.getMessage());
        }
    }
    private void displayData() {
        textArea.setText(""); // Clear previous content
        for (List<String> row : data) {
            String rowData = String.join("\t", row);
            textArea.append(rowData + "\n");
        }
    }
    private void initializeUI() {
        frame = new JFrame("File Chooser Example");
        JButton button = new JButton("Select CSV or Excel File");
        textArea = new JTextArea();
        textArea.setEditable(false);
        frame.setLayout(new BorderLayout());
        frame.add(button, BorderLayout.NORTH);
        frame.add(new JScrollPane(textArea), BorderLayout.CENTER);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFileChooser();
            }
        });



        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void showFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(frame);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            processFile(file);
        }
    }


    private void loadDataFromDatabase() {
        String dbUrl = "jdbc:mysql://localhost:3306/car";
        String dbUsername = "root";
        String dbPassword = "root";
        String query = "SELECT * FROM bmw";
        DataIngestionService.loadDataFromDatabase(dbUrl, dbUsername, dbPassword, query, data);
        for(List<String> row:data) {
            System.out.println(row);
        }
    }


    private void displayFileContent(File file) {
        textArea.setText("");
        try {
            List<List<String>> data;
            if (file.getName().endsWith(".csv")) {
                String csvFilePath = file.getAbsolutePath();
                data = DataIngestionService.loadDataFromCSV(csvFilePath);
            } else if (file.getName().endsWith(".xls") || file.getName().endsWith(".xlsx")) {
                String excelFilePath = file.getAbsolutePath();
                data = DataIngestionService.loadDataFromExcel(excelFilePath);
            } else {
                throw new IllegalArgumentException("Unsupported file type: " + file.getName());
            }

            for (List<String> row : data) {
                String rowData = String.join("\t", row);
                System.out.println(rowData);
                textArea.append(rowData + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
            textArea.setText("Error reading file: " + e.getMessage());
        }
    }
    public void loadDataFromDatabase(String dbUrl, String dbUsername, String dbPassword, String query) {
        data = new ArrayList<>();
        DataIngestionService.loadDataFromDatabase(dbUrl, dbUsername, dbPassword, query, data);
        displayData();
    }
    public List<List<String>> getData() {
        return data;
    }

}
