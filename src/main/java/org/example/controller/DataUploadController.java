package org.example.controller;

import org.example.Service.DataIngestionService;

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
        this.dataIngestionService = new DataIngestionService();
        initializeUI();
        loadDataFromDatabase(); // Call to load data from the database
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


        // Frame settings
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void showFileChooser() {
        // Create a file chooser
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose a CSV or Excel file");
        // Set the file filter for CSV and Excel files
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV and Excel Files", "csv", "xls", "xlsx"));
        // Show the open dialog
        int userSelection = fileChooser.showOpenDialog(frame);
        // Handle the user's selection
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToOpen = fileChooser.getSelectedFile();
            System.out.println("Selected file: " + fileToOpen.getAbsolutePath());
            displayFileContent(fileToOpen);
        } else {
            System.out.println("Open command cancelled by user.");
        }

    }


    private void loadDataFromDatabase() {
        String dbUrl = "jdbc:mysql://localhost:3306/car"; // Replace with your DB URL
        String dbUsername = "root"; // Replace with your DB username
        String dbPassword = "root"; // Replace with your DB password
        String query = "SELECT * FROM bmw"; // SQL Query
        DataIngestionService.loadDataFromDatabase(dbUrl, dbUsername, dbPassword, query, data);
        for(List<String> row:data) {
            System.out.println(row); // Join each row's contents with tabs
        }
    }


    private void displayFileContent(File file) {
        textArea.setText(""); // Clear previous content
        try {
            if (file.getName().endsWith(".csv")) {
                // Read CSV file
                String csvFilePath = file.getAbsolutePath();
                dataIngestionService.loadDataFromCSV(csvFilePath, data); // Call the instance method
            } else if (file.getName().endsWith(".xls") || file.getName().endsWith(".xlsx")) {
                // Read Excel file and store data in the list
                dataIngestionService.loadDataFromExcel(file.getAbsolutePath(), data); // Call the instance method
            }
            for(List<String> row:data) {
                System.out.println(row); // Join each row's contents with tabs
            }
        } catch (Exception e) {
            e.printStackTrace();
            textArea.setText("Error reading file: " + e.getMessage());
        }
}


}

