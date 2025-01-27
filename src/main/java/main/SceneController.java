package main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneController {

    @FXML
    private TextField modelField; // TextField for "Model"
    @FXML
    private TextField yearField; // TextField for "Year"
    @FXML
    private TextField priceField; // TextField for "Price"
    @FXML
    private TextField mileageField; // TextField for "Mileage"
    @FXML
    private TextField taxField; // TextField for "Tax"
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
        myChoiceBox.getItems().addAll("Sedan", "SUV", "Hatchback", "Convertible");
        trans.getItems().addAll("Manual", "Automatic");
        fuel.getItems().addAll("Petrol", "Diesel", "Electric", "Hybrid");

        // Optionally, set default values for ChoiceBoxes
        myChoiceBox.setValue("Sedan");
        trans.setValue("Manual");
        fuel.setValue("Petrol");

        // Initialize TextFields with placeholders or empty strings
        modelField.setText("");
        yearField.setText("");
        priceField.setText("");
        mileageField.setText("");
        taxField.setText("");
        mpgField.setText("");
        engineSizeField.setText("");
    }

    @FXML
    public void goToNewPage() {
        //if (isFormValid()) {
            try {
                // Load new FXML and transition to the new scene
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/MLPage.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) goToNewPageButton.getScene().getWindow();
                // Access the controller
                MLSceneController controller = loader.getController();

                // Dynamically set the label text
                controller.setLabelText("32.000");

                Scene scene = new Scene(root);
                stage.setScene(scene);
            } catch (IOException e) {
               //// e.printStackTrace();
            }

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
            //e.printStackTrace(); // Handle loading error
        }
    }

    @FXML
    private void onAddDataClicked() {
        try {
            Main.processData(); // Assuming Main has a static method `processData`
        } catch (Exception e) {
            //e.printStackTrace();
            // Optionally, handle any errors here
        }
    }}

    /*private boolean isFormValid() {
        StringBuilder validationErrors = new StringBuilder();

        // Check for empty fields
        if (modelField.getText().trim().isEmpty()) validationErrors.append("Model is required.\n");
        if (yearField.getText().trim().isEmpty()) validationErrors.append("Year is required.\n");
        if (priceField.getText().trim().isEmpty()) validationErrors.append("Price is required.\n");
        if (mileageField.getText().trim().isEmpty()) validationErrors.append("Mileage is required.\n");
        if (taxField.getText().trim().isEmpty()) validationErrors.append("Tax is required.\n");
        if (mpgField.getText().trim().isEmpty()) validationErrors.append("MPG is required.\n");
        if (engineSizeField.getText().trim().isEmpty()) validationErrors.append("Engine Size is required.\n");
        if (myChoiceBox.getValue() == null) validationErrors.append("Car Type is required.\n");
        if (trans.getValue() == null) validationErrors.append("Transmission is required.\n");
        if (fuel.getValue() == null) validationErrors.append("Fuel Type is required.\n");

        // Validate numeric inputs and append error messages for invalid fields
        if (!isNumeric(yearField.getText())) validationErrors.append("Year must be a valid number.\n");
        if (!isNumeric(priceField.getText())) validationErrors.append("Price must be a valid number.\n");
        if (!isNumeric(mileageField.getText())) validationErrors.append("Mileage must be a valid number.\n");
        if (!isNumeric(taxField.getText())) validationErrors.append("Tax must be a valid number.\n");
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
}*/
