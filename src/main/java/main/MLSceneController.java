package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class MLSceneController {

    @FXML
    private ImageView histogramEngineSizeImageView;
    @FXML private ImageView histogramMileageImageView;
    @FXML private ImageView histogramMpgImageView;

    @FXML private ImageView histogramPriceImageView;
    @FXML private ImageView histogramRoadTaxImageView;
    @FXML private ImageView histogramYearImageView;

    @FXML private ImageView pieChartFuelTypeImageView;
    @FXML private ImageView pieChartTransmissionImageView;
    @FXML private ImageView pieChartCarTypeImageView;

    @FXML private ImageView scatterPlotEngineSizeVsPriceImageView;
    @FXML private ImageView scatterPlotMileageVsPriceImageView;
    @FXML private ImageView scatterPlotMpgVsPriceImageView;
    @FXML private ImageView scatterPlotYearVsPriceImageView;
    @FXML
    private Button backToMainButton;
    @FXML
    private Label myLabel;

    // Method to set text dynamically
    public void setLabelText(String text) {
        myLabel.setText(text);
    }
    @FXML
    private void initialize() {
        histogramEngineSizeImageView.setImage(new Image("https://www.w3schools.com/w3images/fjords.jpg"));

        histogramMileageImageView.setImage(new Image("https://www.w3schools.com/w3images/fjords.jpg"));
        histogramMpgImageView.setImage(new Image("https://www.w3schools.com/w3images/fjords.jpg"));

        histogramPriceImageView.setImage(new Image("https://www.w3schools.com/w3images/fjords.jpg"));
        histogramRoadTaxImageView.setImage(new Image("file:///C:/Users/LENOVO/Desktop/Junior/project_oop_version2/data/categorical_data/histogram_roadTax.png"));
        histogramYearImageView.setImage(new Image("file:///C:/Users/LENOVO/Desktop/Junior/project_oop_version2/data/categorical_data/histogram_year.png"));

        pieChartFuelTypeImageView.setImage(new Image("https://www.w3schools.com/w3images/fjords.jpg"));
        pieChartTransmissionImageView.setImage(new Image("file:///C:/Users/LENOVO/Desktop/Junior/project_oop_version2/data/categorical_data/pie_chart_transmission.png"));
        pieChartCarTypeImageView.setImage(new Image("file:///C:/Users/LENOVO/Desktop/Junior/project_oop_version2/data/categorical_data/pie_chart_typeCar.png"));

        scatterPlotEngineSizeVsPriceImageView.setImage(new Image("file:///C:/Users/LENOVO/Desktop/Junior/project_oop_version2/data/numerical_data/scatter_plot_engineSize_vs_price.png"));
        scatterPlotMileageVsPriceImageView.setImage(new Image("https://www.w3schools.com/w3images/fjords.jpg"));
        scatterPlotMpgVsPriceImageView.setImage(new Image("file:///C:/Users/LENOVO/Desktop/Junior/project_oop_version2/data/numerical_data/scatter_plot_mpg_vs_price.png"));
        scatterPlotYearVsPriceImageView.setImage(new Image("file:///C:/Users/LENOVO/Desktop/Junior/project_oop_version2/data/numerical_data/scatter_plot_year_vs_price.png"));
    }
    // Handle going back to the main page
    @FXML
    private void goBackToMainPage(ActionEvent event) {
        try {
            // Load the main page FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/main_scene.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) backToMainButton.getScene().getWindow();

            // Set the new scene (main page) on the current stage
            stage.setScene(scene);

            // Show the new scene
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
