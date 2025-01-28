package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SceneController {

    @FXML
    private TextField modelField; // TextField for "Model"
    @FXML
    private TextField yearField; // TextField for "Year"
    @FXML
    private TextField mileageField; // TextField for "Mileage"
    @FXML
    private TextField mpgField; // TextField for "Mpg"
    @FXML
    private TextField engineSizeField; // TextField for "Engine Size"

    @FXML
    private ChoiceBox<String> myChoiceBox; // ChoiceBox for car type
    @FXML
    private ChoiceBox<String> trans; // ChoiceBox for transmission
    @FXML
    private ChoiceBox<String> fuel; // ChoiceBox for fuel type

    @FXML
    private Button goToNewPageButton; // Button to navigate to the new page
    @FXML
    private Button goToDashboardButton; // Button to navigate to the Dashboard

    // Initialize method for setting up the controller
    @FXML
    public void initialize() {
        // Initialize ChoiceBoxes with example data
        myChoiceBox.getItems().addAll("Audi", "Merc", "BMW", "Toyota");
        trans.getItems().addAll("Manual", "Automatic");
        fuel.getItems().addAll("Petrol", "Diesel", "Electric", "Hybrid");

        // Optionally, set default values for ChoiceBoxes
        myChoiceBox.setValue("Audi");
        trans.setValue("Manual");
        fuel.setValue("Petrol");

        // Initialize TextFields with placeholders or empty strings
        modelField.setText("");
        yearField.setText("");
        mileageField.setText("");
        mpgField.setText("");
        engineSizeField.setText("");
    }

    @FXML
    public void sendDataToBackend() {
        // Collect data from input fields and choice boxes
        // Use LinkedHashMap instead of HashMap to preserve insertion order
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("mileage", parseInteger(mileageField.getText()));
        data.put("year", parseInteger(yearField.getText()));
        data.put("engineSize", parseDouble(engineSizeField.getText()));
        data.put("mpg", parseDouble(mpgField.getText()));
        data.put("transmission", trans.getValue());
        data.put("carType", myChoiceBox.getValue());
        data.put("model", modelField.getText());
        data.put("fuelType", fuel.getValue());

        try {
            // Convert data map to JSON string
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(data);

            System.out.println("Sending JSON: " + json);
            // Create an HTTP client
            HttpClient client = HttpClient.newHttpClient();

            // Create a POST HTTP request with JSON payload
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:4567/predict")) // Replace with your backend URL
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            // Send the request and handle the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Print the response (optional)
            System.out.println("Response code: " + response.statusCode());
            System.out.println("Response body: " + response.body());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Integer parseInteger(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null; // Return null if parsing fails
        }
    }

    private Double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null; // Return null if parsing fails
        }
    }
    @FXML
    public void goToNewPage() {
        if (isFormValid()) {
            try {
                // Load new FXML and transition to the new scene
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/MLPage.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) goToNewPageButton.getScene().getWindow();
                // Access the controller
                MLSceneController controller = loader.getController();

                // Dynamically set the label text
                controller.setLabelText("27.000");

                Scene scene = new Scene(root);
                stage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }}

    }

    @FXML
    public void goToDashboard() {
        try {
            // Load the Dashboard.fxml and transition to the dashboard scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/Dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) goToDashboardButton.getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace(); // Handle loading error
        }
    }

    @FXML
    private void onAddDataClicked() {
        try {
            Main.processData(); // Assuming Main has a static method `processData`
        } catch (Exception e) {
            e.printStackTrace();
            // Optionally, handle any errors here
        }
    }

    private boolean isFormValid() {
        StringBuilder validationErrors = new StringBuilder();

        // Check for empty fields
        if (modelField.getText().trim().isEmpty()) validationErrors.append("Model is required.\n");
        if (yearField.getText().trim().isEmpty()) validationErrors.append("Year is required.\n");
        if (mileageField.getText().trim().isEmpty()) validationErrors.append("Mileage is required.\n");
        if (mpgField.getText().trim().isEmpty()) validationErrors.append("MPG is required.\n");
        if (engineSizeField.getText().trim().isEmpty()) validationErrors.append("Engine Size is required.\n");
        if (myChoiceBox.getValue() == null) validationErrors.append("Car Type is required.\n");
        if (trans.getValue() == null) validationErrors.append("Transmission is required.\n");
        if (fuel.getValue() == null) validationErrors.append("Fuel Type is required.\n");

        // Validate numeric inputs and append error messages for invalid fields
        if (!isNumeric(yearField.getText())) validationErrors.append("Year must be a valid number.\n");
        if (!isNumeric(mileageField.getText())) validationErrors.append("Mileage must be a valid number.\n");
        if (!isNumeric(mpgField.getText())) validationErrors.append("MPG must be a valid number.\n");
        if (!isNumeric(engineSizeField.getText())) validationErrors.append("Engine Size must be a valid number.\n");

        if (validationErrors.length() > 0) {
            showAlert("Validation Error", validationErrors.toString());
            return false;
        }

        return true; // All checks passed
    }

    private boolean isNumeric(String str) {
        return str.matches("\\d+(\\.\\d+)?"); // Matches integers and decimals
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
