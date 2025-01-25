/*package service.MachineLearning;

import static spark.Spark.*;
import com.google.gson.Gson;

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
                return new Gson().toJson(new ErrorResponse("An error occurred while making the prediction."));
            }
        });

        System.out.println("Server started. Listening on port 4567...");
    }

    // Parse input data from the request body
    private double[] parseInputData(String requestBody) {
        // Example: {"features": [4.0, 5.0, 6.0]}
        // Use Gson to parse the JSON input
        Gson gson = new Gson();
        PredictionRequest request = gson.fromJson(requestBody, PredictionRequest.class);
        return request.features;
    }

    // Inner class to represent the prediction request
    private static class PredictionRequest {
        double[] features;
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
}*/