package lk.ijse.supermarket.supermarkethibernate2.controller;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML
    private Label head;

    @FXML
    private ImageView customer2;

    @FXML
    private ImageView item2;

    @FXML
    private Label lblDescription;

    @FXML
    private Label lblMenu;

    @FXML
    private ImageView order2;

    @FXML
    private ImageView imgX;

    @FXML
    private AnchorPane root;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), root);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
        playTypewriterAnimation("Supermarket-Hibernate");

    }

    @FXML
    void onMouseExited(MouseEvent event) {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();
            ScaleTransition scaleT = new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1);
            scaleT.setToY(1);
            scaleT.play();

            icon.setEffect(null);
            lblMenu.setText("Welcome");
            lblDescription.setText("Please select one of above main operations to proceed");
        }
    }
    @FXML
    void onMouseEntered(MouseEvent event) {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();

            switch (icon.getId()) {
                case "customer2":
                    lblMenu.setText("Manage Customers");
                    lblDescription.setText("Click to add, edit, delete, search or view customers");
                    break;
                case "item2":
                    lblMenu.setText("Manage Items");
                    lblDescription.setText("Click to add, edit, delete, search or view items");
                    break;
                case "order2":
                    lblMenu.setText("Place Orders");
                    lblDescription.setText("Click here if you want to place a new order");
                    break;
            }

            ScaleTransition scaleT = new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1.2);
            scaleT.setToY(1.2);
            scaleT.play();

            DropShadow glow = new DropShadow();
            glow.setColor(Color.CORNFLOWERBLUE);
            glow.setWidth(20);
            glow.setHeight(20);
            glow.setRadius(20);
            icon.setEffect(glow);
        }
    }

    private void playTypewriterAnimation(String text) {
        final int[] charIndex = {0};
        final Timeline timeline = new Timeline();

        KeyFrame keyFrame = new KeyFrame(Duration.millis(100), event -> {
            if (charIndex[0] < text.length()) {
                head.setText(head.getText() + text.charAt(charIndex[0]++));
            } else {
                timeline.stop();

                // Pause for 2 seconds before restarting the animation
                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                pause.setOnFinished(e -> {
                    head.setText("");
                    charIndex[0] = 0;
                    timeline.playFromStart();
                });
                pause.play();
            }
        });

        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    @FXML
    void navigate(MouseEvent event) throws IOException {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();

            Parent root = null;

            switch (icon.getId()) {
                case "customer2":
                    root = FXMLLoader.load(this.getClass().getResource("/view/customer.fxml"));
                    break;
                case "item2":
                    root = FXMLLoader.load(this.getClass().getResource("/view/item.fxml"));
                    break;
                case "order2":
                    root = FXMLLoader.load(this.getClass().getResource("/view/order.fxml"));

            }

            if (root != null) {
                Scene scene = new Scene(root);
                Stage primaryStage = (Stage) this.root.getScene().getWindow();
                primaryStage.setScene(scene);
                primaryStage.centerOnScreen();

                TranslateTransition transition = new TranslateTransition(Duration.millis(350), scene.getRoot());
                transition.setFromX(-scene.getWidth());
                transition.setToX(0);
                transition.play();
            }
        }
    }

    @FXML
    void close(MouseEvent event) {
        System.out.println("Bye..!");
        System.exit(0);

    }





}