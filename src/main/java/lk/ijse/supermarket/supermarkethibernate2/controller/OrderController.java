package lk.ijse.supermarket.supermarkethibernate2.controller;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class OrderController implements Initializable {

    @FXML
    private ComboBox<?> cmbCustomerID;

    @FXML
    private ComboBox<?> cmbItemID;

    @FXML
    private ImageView imgHome;

    @FXML
    private ImageView imgResize;

    @FXML
    private Label lblCustomer;

    @FXML
    private AnchorPane root;

    @FXML
    private TextField txtQty;

    @FXML
    private Button btnAdd;



    public void onHomeClick(MouseEvent mouseEvent) throws IOException {
        URL resource = this.getClass().getResource("/view/dashboard.fxml");
        Parent root = FXMLLoader.load(resource);
        Scene scene = new Scene(root);
        Stage primaryStage = (Stage) (this.root.getScene().getWindow());
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        Platform.runLater(() -> primaryStage.sizeToScene());
    }

    public void imgResizeClick(MouseEvent mouseEvent) {
        Stage stage = (Stage) imgResize.getScene().getWindow();
        stage.setFullScreen(true);
    }

    private void applyFadeAnimation(Label label) {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), label);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.setCycleCount(1);

        FadeTransition fadeOut = new FadeTransition(Duration.millis(1000), label);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setCycleCount(1);
        fadeOut.setDelay(Duration.seconds(1));

        fadeIn.setOnFinished(event -> fadeOut.play());
        fadeOut.setOnFinished(event -> fadeIn.play());

        fadeIn.play();
    }

    private void addBubbleEffect(Button button) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(button.translateYProperty(), 0)),
                new KeyFrame(Duration.seconds(1), new KeyValue(button.translateYProperty(), -10)),
                new KeyFrame(Duration.seconds(2), new KeyValue(button.translateYProperty(), 0))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        applyFadeAnimation(lblCustomer);
        addBubbleEffect(btnAdd);
    }
}
