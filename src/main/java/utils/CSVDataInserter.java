package utils;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CSVDataInserter {

    private static final String INSERT_SQL = "INSERT INTO cars " +
            "(model, year, price, transmission, mileage, fuel_type, tax, mpg, engine_size, car_type) \n" +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    // Method to insert data into the database
    public void insertDataToDatabase(String csvFilePath) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            // Establish connection
            DatabaseConnector connector = DatabaseConnector.getInstance();
            connection = connector.getConnection();
            

            // Prepare SQL statement
            statement = connection.prepareStatement(INSERT_SQL);

            // Open the CSV file using OpenCSV's CSVReader
            CSVReader csvReader = new CSVReader(new FileReader(csvFilePath));
            String[] data;

            // Skip header if the CSV has a header row (optional)
            csvReader.readNext(); // This skips the header row

            // Read each row of the CSV file
            while ((data = csvReader.readNext()) != null) {
                // Print the row to verify correct data reading
                //System.out.println("Reading data: " + String.join(", ", data));

                if (data.length == 10) {  // Ensure 10 columns per row
                    statement.setString(1, data[0]);  // model
                    statement.setInt(2, Integer.parseInt(data[1]));  // year
                    statement.setBigDecimal(3, new BigDecimal(data[2]));  // price
                    statement.setString(4, data[3]);  // transmission
                    statement.setInt(5, Integer.parseInt(data[4]));  // mileage
                    statement.setString(6, data[5]);  // fuel_type
                    statement.setBigDecimal(7, new BigDecimal(data[6]));  // tax
                    statement.setBigDecimal(8, new BigDecimal(data[7]));  // mpg
                    statement.setBigDecimal(9, new BigDecimal(data[8]));  // engine_size
                    statement.setString(10, data[9]);  // car_type (you might extract this from the file name if needed)

                    // Execute the update (insert)
                    statement.executeUpdate();
                }
                else {
                    System.out.println("Skipping invalid row: " + String.join(", ", data));
                }
            }

            csvReader.close();
            System.out.println("Data successfully inserted into database from CSV.");
        } catch (SQLException | IOException | CsvValidationException e) {
            System.out.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

