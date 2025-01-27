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
    private ImageView msemodel;
    @FXML
    private ImageView maemodel;
    @FXML
    private ImageView rmsemodel;
    @FXML
    private ImageView mapemodel;
    @FXML
    private ImageView medmodel;
    @FXML
    private ImageView rmodel;

    @FXML
    private ImageView msevt;
    @FXML
    private ImageView maevt;
    @FXML
    private ImageView rmsevt;
    @FXML
    private ImageView mapevt;
    @FXML
    private ImageView medvt;
    @FXML
    private ImageView rvt;

    @FXML
    private ImageView msec;
    @FXML
    private ImageView maec;
    @FXML
    private ImageView rmsec;
    @FXML
    private ImageView mapec;
    @FXML
    private ImageView medc;
    @FXML
    private ImageView rc;

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
        msemodel.setImage(new Image("data/ML/MSE model.png"));
        maemodel.setImage(new Image("data/ML/MAE Model.png"));
        rmsemodel.setImage(new Image("data/ML/RMSE Model.png"));
        mapemodel.setImage(new Image("data/ML/Mape Model.png"));
        medmodel.setImage(new Image("data/ML/Med model.png"));
        rmodel.setImage(new Image("data/ML/R model.png"));

        msevt.setImage(new Image("data/ML/mse vt.png"));
        maevt.setImage(new Image("data/ML/mae vt.png"));
        rmsevt.setImage(new Image("data/ML/rmse vt.png"));
        mapevt.setImage(new Image("data/ML/mape vt.png"));
        medvt.setImage(new Image("data/ML/med vt.png"));
        rvt.setImage(new Image("data/ML/r vt.png"));

        msec.setImage(new Image("data/ML/mse c.png"));
        maec.setImage(new Image("data/ML/mae c.png"));
        rmsec.setImage(new Image("data/ML/rmse c.png"));
        mapec.setImage(new Image("data/ML/mape c.png"));
        medc.setImage(new Image("data/ML/med c.png"));
        rc.setImage(new Image("data/ML/r c.png"));

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
