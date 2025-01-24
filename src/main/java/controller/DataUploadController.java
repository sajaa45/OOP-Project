package controller;

import interfaces.DataUploadInterface;
import service.DataImporting.FileProcessorService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import service.DataImporting.DatabaseProcessorService;


public class DataUploadController implements DataUploadInterface {
    private JFrame frame;
    private JTextArea textArea;
    private List<List<String>> data;

    public DataUploadController() {
        this.data = new ArrayList<>();
        initializeUI();
    }

    @Override
    public void processFile(File file) {
        try {
            data = FileProcessorService.processFile(file);
            displayData();
        } catch (Exception e) {
            e.printStackTrace();
            textArea.setText("Error processing file: " + e.getMessage());
        }
    }

    @Override
    public void loadDataFromDatabase(String dbUrl, String dbUsername, String dbPassword, String query) {
        data = new ArrayList<>();
        DatabaseProcessorService.loadDataFromDatabase(dbUrl, dbUsername, dbPassword, query, data);
        displayData();
    }

    @Override
    public List<List<String>> getData() {
        return data;
    }

    private void initializeUI() {
        frame = new JFrame("Data Upload Example");
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1)); // Two buttons: one for file and one for database

        JButton fileButton = new JButton("Select CSV or Excel File");
        JButton databaseButton = new JButton("Load Data from Database");

        textArea = new JTextArea();
        textArea.setEditable(false);

        panel.add(fileButton);
        panel.add(databaseButton);

        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.NORTH);
        frame.add(new JScrollPane(textArea), BorderLayout.CENTER);

        // File Button Action Listener
        fileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFileChooser();
            }
        });

        // Database Button Action Listener
        databaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadDataFromDatabaseUI();
            }
        });

        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void loadDataFromDatabaseUI() {
        JTextField dbUrlField = new JTextField("jdbc:mysql://localhost:3306/car");
        JTextField usernameField = new JTextField("root");
        JPasswordField passwordField = new JPasswordField();
        JTextField queryField = new JTextField("SELECT * FROM cars");

        Object[] message = {
                "Database URL:", dbUrlField,
                "Username:", usernameField,
                "Password:", passwordField,
                "Query:", queryField
        };

        int option = JOptionPane.showConfirmDialog(frame, message, "Database Connection", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String dbUrl = dbUrlField.getText();
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String query = queryField.getText();

            try {
                loadDataFromDatabase(dbUrl, username, password, query);
                JOptionPane.showMessageDialog(frame, "Data loaded successfully!");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error loading data from database: " + e.getMessage());
            }
        }
    }

    private void showFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(frame);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            processFile(file);
        }
    }

    private void displayData() {
        textArea.setText(""); // Clear previous content
        for (List<String> row : data) {
            String rowData = String.join("\t", row);
            textArea.append(rowData + "\n");
        }
    }
}





/*package controller;

import javax.swing.*;
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

}*/
