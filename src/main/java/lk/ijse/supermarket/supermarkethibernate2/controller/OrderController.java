package lk.ijse.supermarket.supermarkethibernate2.controller;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import lk.ijse.supermarket.supermarkethibernate2.bo.custom.CustomerBO;
import lk.ijse.supermarket.supermarkethibernate2.bo.custom.ItemBO;
import lk.ijse.supermarket.supermarkethibernate2.bo.custom.OrderBO;
import lk.ijse.supermarket.supermarkethibernate2.bo.custom.PlaceOrderBO;
import lk.ijse.supermarket.supermarkethibernate2.dto.CustomerDTO;
import lk.ijse.supermarket.supermarkethibernate2.dto.ItemDTO;
import lk.ijse.supermarket.supermarkethibernate2.dto.OrderDTO;
import lk.ijse.supermarket.supermarkethibernate2.dto.OrderDetailsDTO;
import lk.ijse.supermarket.supermarkethibernate2.entity.OrderDetails;
import lk.ijse.supermarket.supermarkethibernate2.view.tdm.CartTM;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;


public class OrderController implements Initializable {

    @FXML
    private Button btnAdd;

    @FXML
    private ComboBox<String> cmbCustomerID;

    @FXML
    private ComboBox<String> cmbItemID;

    @FXML
    private TableColumn<CartTM, String> colItemId;

    @FXML
    private TableColumn<CartTM, String> colItemName;

    @FXML
    private TableColumn<CartTM, Integer> colQty;

    @FXML
    private TableColumn<CartTM, Button> colRemove;

    @FXML
    private TableColumn<CartTM, Double> colTotal;

    @FXML
    private TableColumn<CartTM, Double> colnitPrice;

    @FXML
    private ImageView imgHome;

    @FXML
    private ImageView imgResize;

    @FXML
    private Label lblCustomer;

    @FXML
    private Label lblCustomerName;

    @FXML
    private Label lblItemName;

    @FXML
    private Label lblOrderDate;

    @FXML
    private Label lblOrderId;

    @FXML
    private Label lblQty;

    @FXML
    private Label lblUnitPrice;

    @FXML
    private AnchorPane root;

    @FXML
    private TableView<CartTM> tblOrder;

    @FXML
    private TextField txtQty;

    CustomerBO customerBO = (CustomerBO) BOFactory.getInstance().getBO(BOFactory.BOType.Customer);
    ItemBO itemBO = (ItemBO) BOFactory.getInstance().getBO(BOFactory.BOType.Item);
    OrderBO orderBO = (OrderBO) BOFactory.getInstance().getBO(BOFactory.BOType.Order);

    private final ObservableList<CartTM> cartTMS = FXCollections.observableArrayList();

    private void setCellValues() {
        colItemId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("cartQuantity"));
        colnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colRemove.setCellValueFactory(new PropertyValueFactory<>("removeBtn"));

        tblOrder.setItems(cartTMS);
    }

    private void refreshPage() {
        lblOrderId.setText(orderBO.getLastPk().get());
        lblOrderDate.setText(LocalDate.now().toString());

        setCustomerIDs();
        setItemIDs();

        cmbCustomerID.getSelectionModel().clearSelection();
        cmbItemID.getSelectionModel().clearSelection();
        lblItemName.setText("");
        lblCustomerName.setText("");
        lblQty.setText("");
        lblUnitPrice.setText("");
        txtQty.setText("");

        cartTMS.clear();
        tblOrder.refresh();

        cmbCustomerID.setDisable(false);
    }


    @FXML
    void onAddToCartClick(MouseEvent event) {
        System.out.println("add to cart click");

        String selectedItemId = cmbItemID.getValue();

        if (selectedItemId == null) {
            new Alert(Alert.AlertType.ERROR, "Please select item..!", ButtonType.OK).show();
            return;
        }

        String quantityPattern = "^[0-9]+$";

        boolean isValidQty = txtQty.getText().matches(quantityPattern);

        if (!isValidQty) {
            new Alert(Alert.AlertType.ERROR, "Please enter a valid quantity", ButtonType.OK).show();
            return;
        }

        String itemName = lblItemName.getText();
        int cartQty = Integer.parseInt(txtQty.getText());
        int qtyOnHand = Integer.parseInt(lblQty.getText());

        if (qtyOnHand<cartQty) {
            new Alert(Alert.AlertType.ERROR, "Not enough items..!", ButtonType.OK).show();
            return;
        }

        txtQty.setText("");

        double unitPrice = Double.parseDouble(lblUnitPrice.getText());
        double total = unitPrice * cartQty;

        for (CartTM cartTM : cartTMS) {

            if (cartTM.getItemId().equals(selectedItemId)) {
                int newQty = cartTM.getCartQuantity() + cartQty;

                lblQty.setText(String.valueOf(newQty));

                cartTM.setCartQuantity(newQty);
                cartTM.setTotal(unitPrice * newQty);

                tblOrder.refresh();
                return;
            }
        }

        Button btn = new Button("Remove");

        CartTM newCartTM = new CartTM(
                selectedItemId,
                itemName,
                cartQty,
                unitPrice,
                total,
                btn
        );

        btn.setOnAction(actionEvent -> {
            cartTMS.remove(newCartTM);
            tblOrder.refresh();
        });

        cartTMS.add(newCartTM);
        cmbCustomerID.setDisable(true);

    }


    @FXML
    void onPlaceOrderClick(MouseEvent event) {
        System.out.println("place order click");

        if (tblOrder.getItems().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please add items to cart..!", ButtonType.OK).show();
            return;
        }

        if (cmbCustomerID.getSelectionModel().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please select a customer for place order...!", ButtonType.OK).show();
            return;
        }

        String orderId = lblOrderId.getText();
        Date dateOfOrder = Date.valueOf(lblOrderDate.getText());
        String customerId = cmbCustomerID.getValue();

        ArrayList<OrderDetailsDTO> orderDetailsDTOS = new ArrayList<>();

        for (CartTM cartTM : cartTMS) {
            OrderDetailsDTO orderDetailsDTO = new OrderDetailsDTO(
                    orderId,
                    cartTM.getItemId(),
                    cartTM.getCartQuantity(),
                    cartTM.getUnitPrice()
            );

            orderDetailsDTOS.add(orderDetailsDTO);
        }

        OrderDTO orderDTO = new OrderDTO(
                orderId,
                customerId,
                dateOfOrder,
                orderDetailsDTOS
        );

        PlaceOrderBO placeOrderBO = (PlaceOrderBO) BOFactory.getInstance().getBO(BOFactory.BOType.PlaceOrder);
        boolean isSaved = placeOrderBO.placeOrder(orderDTO);

        if (isSaved) {
            new Alert(Alert.AlertType.INFORMATION, "Order saved successfully..!!", ButtonType.OK).show();
            refreshPage();
        } else {
            new Alert(Alert.AlertType.ERROR, "Order not saved..!!", ButtonType.OK).show();
        }

    }

    @FXML
    void onResetClick(MouseEvent event) {
        System.out.println("reset click");
        refreshPage();

    }

    @FXML
    void onCmbCustomer(ActionEvent event) {
        String selectedCustomerId = cmbCustomerID.getSelectionModel().getSelectedItem();

        if (selectedCustomerId != null) {
            System.out.println("Selected customer Id: " + selectedCustomerId);
            Optional<CustomerDTO> customerDTO = customerBO.findByPk(selectedCustomerId);

            if (customerDTO.isPresent()){
                System.out.println("customer Name: " + customerDTO.get().getName());
                lblCustomerName.setText(customerDTO.get().getName());
            }
        }


    }

    private void setCustomerIDs() {
        ArrayList<String> customerIDs = customerBO.getAllCustomerIds();
        System.out.println("customerIds: " + customerIDs);
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(customerIDs);
        cmbCustomerID.setItems(observableList);
    }

    @FXML
    void onCmbItem(ActionEvent event) {
        String selectedItemId = cmbItemID.getSelectionModel().getSelectedItem();

        if (selectedItemId != null) {
            System.out.println("Selected item Id: " + selectedItemId);

            Optional<ItemDTO> itemDTO = itemBO.findByPk(selectedItemId);

            if (itemDTO.isPresent()){
                System.out.println("Item Name: " + itemDTO.get().getItemName());
                lblItemName.setText(itemDTO.get().getItemName());
                lblQty.setText(String.valueOf(itemDTO.get().getQuantity()));
                lblUnitPrice.setText(String.valueOf(itemDTO.get().getPrice()));
            }
        }
    }

    private void setItemIDs() {
        ArrayList<String> itemIDs = itemBO.getAllItemIds();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(itemIDs);
        cmbItemID.setItems(observableList);
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

        setCellValues();
        try {
            refreshPage();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to refresh page", ButtonType.OK).show();
        }
    }
}
