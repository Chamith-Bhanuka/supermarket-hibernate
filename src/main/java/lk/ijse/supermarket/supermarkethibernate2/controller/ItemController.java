package lk.ijse.supermarket.supermarkethibernate2.controller;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.supermarket.supermarkethibernate2.bo.BOFactory;
import lk.ijse.supermarket.supermarkethibernate2.bo.custom.ItemBO;
import lk.ijse.supermarket.supermarkethibernate2.dto.CustomerDTO;
import lk.ijse.supermarket.supermarkethibernate2.dto.ItemDTO;
import lk.ijse.supermarket.supermarkethibernate2.view.tdm.ItemTM;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class ItemController implements Initializable {

    @FXML
    private ImageView imgHome;

    @FXML
    private ImageView imgResize;

    @FXML
    private Label lblItem;

    @FXML
    private AnchorPane root;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnReset;

    @FXML
    private TableColumn<ItemTM, String> colItemId;

    @FXML
    private TableColumn<ItemTM, String> colName;

    @FXML
    private TableColumn<ItemTM, Double> colPrice;

    @FXML
    private TableColumn<ItemTM, Integer> colQty;

    @FXML
    private TableView<ItemTM> tblItem;

    @FXML
    private TextField txtItemId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPrice;

    @FXML
    private TextField txtQty;

    ItemBO itemBO = (ItemBO) BOFactory.getInstance().getBO(BOFactory.BOType.Item);

    public void imgResizeClick(MouseEvent mouseEvent) {
        Stage stage = (Stage) imgResize.getScene().getWindow();
        stage.setFullScreen(true);
    }

    public void onHomeClick(MouseEvent mouseEvent) throws IOException {
        URL resource = this.getClass().getResource("/view/dashboard.fxml");
        Parent root = FXMLLoader.load(resource);
        Scene scene = new Scene(root);
        Stage primaryStage = (Stage) (this.root.getScene().getWindow());
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        Platform.runLater(() -> primaryStage.sizeToScene());
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        applyFadeAnimation(lblItem);

        colItemId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        refreshPage();
    }

    private void refreshPage() {
        refreshTable();

        String nextItemId = itemBO.getLastPk().get();
        txtItemId.setText(nextItemId);
        txtItemId.setEditable(false);

        txtName.setText("");
        txtQty.setText("");
        txtPrice.setText("");

        btnSave.setDisable(false);

        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        btnReset.setDisable(true);
    }

    private void refreshTable() {
        ArrayList<ItemDTO> itemDTOS = (ArrayList<ItemDTO>) itemBO.getAll();

        ObservableList<ItemTM> itemTMS = FXCollections.observableArrayList();

        for (ItemDTO itemDTO : itemDTOS) {
            ItemTM itemTM = new ItemTM(
                    itemDTO.getItemId(),
                    itemDTO.getItemName(),
                    itemDTO.getQuantity(),
                    itemDTO.getPrice()
            );
            itemTMS.add(itemTM);
        }
        tblItem.setItems(itemTMS);
    }


    @FXML
    void onDeleteClick(MouseEvent event) {
        System.out.println("Item delete clicked");
        String itemId = txtItemId.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this item?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get() == ButtonType.YES) {

            boolean isDeleted = itemBO.deleteByPk(itemId);

            if (isDeleted) {
                new Alert(Alert.AlertType.INFORMATION, "Item deleted...!").show();
                refreshPage();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to delete item...!").show();
            }
        }



    }

    @FXML
    void onResetClick(MouseEvent event) {
        System.out.println("Item reset clicked");
        refreshPage();

    }

    @FXML
    void onSaveClick(MouseEvent event) {
        System.out.println("Item save clicked");

        String id = txtItemId.getText();
        String name = txtName.getText();
        double price = Double.parseDouble(txtPrice.getText());
        int quantity = Integer.parseInt(txtQty.getText());

        ItemDTO itemDTO = new ItemDTO(id, name, quantity, price);

        boolean isSaved = itemBO.save(itemDTO);

        if (isSaved) {
            new Alert(Alert.AlertType.INFORMATION, "Item has been saved").show();
            refreshPage();
        } else {
            new Alert(Alert.AlertType.ERROR, "Item has not been saved").show();
        }

    }

    @FXML
    void onUpdateClick(MouseEvent event) {
        System.out.println("Item update clicked");

        String id = txtItemId.getText();
        String name = txtName.getText();
        double price = Double.parseDouble(txtPrice.getText());
        int quantity = Integer.parseInt(txtQty.getText());

        ItemDTO itemDTO = new ItemDTO(id, name, quantity, price);

        boolean isSaved = itemBO.update(itemDTO);

        if (isSaved) {
            new Alert(Alert.AlertType.INFORMATION, "Item has been updated").show();
            refreshPage();
        } else {
            new Alert(Alert.AlertType.ERROR, "Item has not been updated").show();
        }

    }

    @FXML
    void onTblItemClick(MouseEvent event) {
        ItemTM itemTM = tblItem.getSelectionModel().getSelectedItem();

        if (itemTM != null) {
            txtItemId.setText(itemTM.getItemId());
            txtItemId.setEditable(false);
            txtName.setText(itemTM.getName());
            txtQty.setText(String.valueOf(itemTM.getQuantity()));
            txtPrice.setText(String.valueOf(itemTM.getPrice()));

            btnSave.setDisable(true);

            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
            btnReset.setDisable(false);
        }

    }

}
