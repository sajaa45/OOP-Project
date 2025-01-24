package service.MachineLearning;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class DataVisualizationMLService {

    // Method to display model results in a JTable within a JFrame
    public void displayModelResultsInJFrame(List<MLModelTrainer.ModelResult> results) {
        // Create JFrame
        JFrame frame = new JFrame("ML Model Results");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);

        // Table column names
        String[] columnNames = {"Model Name", "MSE", "MAE", "R-squared"};

        // Populate table data
        Object[][] data = new Object[results.size()][4];
        for (int i = 0; i < results.size(); i++) {
            MLModelTrainer.ModelResult result = results.get(i);
            data[i][0] = result.getModelName();
            data[i][1] = String.format("%.2f", result.getMse());
            data[i][2] = String.format("%.2f", result.getMae());
            data[i][3] = String.format("%.2f", result.getR2());
        }

        // Create table model and JTable
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(30);

        // Set table header style
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("Arial", Font.BOLD, 16));
        tableHeader.setBackground(Color.LIGHT_GRAY);
        tableHeader.setReorderingAllowed(false);

        // Add table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Add title label
        JLabel titleLabel = new JLabel("Machine Learning Model Results", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        frame.add(titleLabel, BorderLayout.NORTH);

        // Display the frame
        frame.setVisible(true);
    }
}
