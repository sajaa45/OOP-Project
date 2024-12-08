package utils;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

public class CSVMerger {

    // Method to merge multiple CSV files into a single CSV with the car_type column
    public void mergeCSVFiles(String[] fileNames, String outputFileName) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(outputFileName))) {
            // Flag to track if we need to write the header
            boolean isFirstFile = true;

            // Loop through each file and read its content
            for (String fileName : fileNames) {
                try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
                    String[] row;

                    // Read the header from the first file and write it to the output only once
                    if (isFirstFile) {
                        // Read the header of the first file
                        String[] headers = reader.readNext();
                        if (headers != null) {
                            // Add 'car_type' to the header
                            String[] updatedHeaders = new String[headers.length + 1];
                            System.arraycopy(headers, 0, updatedHeaders, 0, headers.length);
                            updatedHeaders[headers.length] = "car_type"; // Adding the car_type column
                            writer.writeNext(updatedHeaders); // Write the updated header to the output
                        }
                        isFirstFile = false; // After the first file, we don't write headers anymore
                    } else {
                        // Skip the header for subsequent files
                        reader.readNext();
                    }

                    // Process each row in the file
                    while ((row = reader.readNext()) != null) {
                        // Get the car type as the file name (without extension)
                        String carType = new File(fileName).getName().replace(".csv", "");

                        // Add the car_type value as the filename (without extension)
                        String[] updatedRow = new String[row.length + 1];
                        System.arraycopy(row, 0, updatedRow, 0, row.length);
                        updatedRow[row.length] = carType; // Use the filename as car_type

                        // Write the row to the output file
                        writer.writeNext(updatedRow);
                    }
                } catch (IOException | CsvValidationException e) {
                    // Handle IO and CSV validation exceptions
                    System.err.println("Error processing file " + fileName + ": " + e.getMessage());
                }
            }

            System.out.println("CSV files merged successfully into " + outputFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



/*package utils;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVMerger {
    public void mergeCSVFiles(String[] inputFiles, String outputFileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
            for (int i = 0; i < inputFiles.length; i++) {
                BufferedReader reader = new BufferedReader(new FileReader(inputFiles[i]));
                String line;

                // Skip header in all but the first file
                if (i > 0) {
                    reader.readLine(); // Skip the header
                }

                while ((line = reader.readLine()) != null) {
                    writer.write(line);
                    writer.newLine();
                }
                reader.close();
            }
            System.out.println("CSV files merged successfully into " + outputFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}*/
