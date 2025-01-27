package service.MachineLearning;

import static spark.Spark.*;
import com.google.gson.Gson;
import model.Car;
import service.MachineLearning.DataPreprocessor; // Import the DataPreprocessor class
import java.util.List;
import java.util.Map;

public class ModelDeploymentService {
    private MLModel mlModel;

    public ModelDeploymentService(MLModel mlModel) {
        this.mlModel = mlModel;
    }

    // Start the server
    public void startServer() {
        // Define the prediction endpoint
        post("/predict", (req, res) -> {
            try {
                // Parse input data from the request
                double[] features = parseInputData(req.body());

                // Make a prediction
                double prediction = mlModel.predict(features);

                // Return the prediction as JSON
                res.type("application/json");
                return new Gson().toJson(new PredictionResponse(prediction));
            } catch (Exception e) {
                res.status(500);
                return new Gson().toJson(new ErrorResponse("An error occurred while making the prediction: " + e.getMessage()));
            }
        });

        System.out.println("Server started. Listening on port 4567...");
    }

    // Parse input data from the request body
    private double[] parseInputData(String requestBody) {
        // Parse the JSON input
        Gson gson = new Gson();
        PredictionRequest request = gson.fromJson(requestBody, PredictionRequest.class);

        // Step 1: Simulate the cleaned data format by adding a dummy price column
        Object[][] rawDataForPCA = new Object[1][9]; // 9 columns: mileage, year, engineSize, mpg, price, transmission, carType, model, fuelType
        rawDataForPCA[0][0] = request.mileage;
        rawDataForPCA[0][1] = request.year;
        rawDataForPCA[0][2] = request.engineSize;
        rawDataForPCA[0][3] = request.mpg;
        rawDataForPCA[0][4] = 0.0; // Dummy price value
        rawDataForPCA[0][5] = request.transmission;
        rawDataForPCA[0][6] = request.carType;
        rawDataForPCA[0][7] = request.model;
        rawDataForPCA[0][8] = request.fuelType;

        // Step 2: Preprocess the data using the DataPreprocessor class
        DataPreprocessor preprocessor = new DataPreprocessor(rawDataForPCA);
        double varianceThreshold = 0.95;
        preprocessor.preprocessAndDisplayData(rawDataForPCA, varianceThreshold);


        // Step 3: Retrieve the preprocessed data
        Object[][] reducedData = preprocessor.getReducedData();

        // Step 4: Force the number of components to be 11
        int nComponents = 11; // Force the number of components
        if (reducedData[0].length != nComponents) {
            // If the reduced data does not have 11 components, pad or truncate it
            double[] paddedFeatures = new double[nComponents];
            for (int i = 0; i < nComponents; i++) {
                if (i < reducedData[0].length) {
                    paddedFeatures[i] = ((Number) reducedData[0][i]).doubleValue();
                } else {
                    paddedFeatures[i] = 0.0; // Pad with zeros if fewer components
                }
            }
            return paddedFeatures;
        }

        // Step 5: Convert the preprocessed data to a double array
        double[] features = new double[reducedData[0].length];
        for (int i = 0; i < reducedData[0].length; i++) {
            features[i] = ((Number) reducedData[0][i]).doubleValue();
        }

        return features;
    }

    // Inner class to represent the prediction request
    private static class PredictionRequest {
        int mileage;        // Mileage (e.g., 31461)
        int year;           // Manufacturing year (e.g., 2016)
        double engineSize;  // Engine size (e.g., 1.6)
        double mpg;         // Miles per gallon (e.g., 76.3)
        String transmission; // Transmission type (e.g., "Manual")
        String carType;     // Car type (e.g., "audi")
        String model;       // Car model (e.g., "A1")
        String fuelType;    // Fuel type (e.g., "Diesel")
    }

    // Inner class to represent the prediction response
    private static class PredictionResponse {
        double prediction;

        PredictionResponse(double prediction) {
            this.prediction = prediction;
        }
    }

    // Inner class to represent the error response
    private static class ErrorResponse {
        String error;

        ErrorResponse(String error) {
            this.error = error;
        }
    }

    // Helper method to convert List<Car> to a 2D array (Object[][]) format
    public static Object[][] convertCarListToDataArray(List<Car> cleanedCarList) {
        if (cleanedCarList == null || cleanedCarList.isEmpty()) {
            return null; // Return null if the list is empty or null
        }

        Object[][] dataArray = new Object[cleanedCarList.size()][];
        for (int i = 0; i < cleanedCarList.size(); i++) {
            Car car = cleanedCarList.get(i);
            if (car != null) {
                dataArray[i] = new Object[]{
                        car.getMileage(),
                        car.getYear(),
                        car.getEngineSize(),
                        car.getMpg(),
                        car.getPrice(),
                        car.getTransmission(),
                        car.getCarType(),
                        car.getModel(),
                        car.getFuelType()
                };
            } else {
                System.err.println("Warning: Null car object found at index " + i);
            }
        }
        return dataArray;
    }
}