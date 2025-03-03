package lk.ijse.supermarket.supermarkethibernate2.controller;

import javafx.animation.FadeTransition;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.supermarket.supermarkethibernate2.bo.BOFactory;
import lk.ijse.supermarket.supermarkethibernate2.bo.custom.CustomerBO;
import lk.ijse.supermarket.supermarkethibernate2.db.DBConnection;
import lk.ijse.supermarket.supermarkethibernate2.dto.CustomerDTO;
import lk.ijse.supermarket.supermarkethibernate2.view.tdm.CustomerTM;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class CustomerController implements Initializable {
    @FXML
    private Label lblCustomer;

    @FXML
    private ImageView imgResize;

    @FXML
    private AnchorPane root;

    @FXML
    private ImageView imgHome;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnGetAllReports;

    @FXML
    private Button btnOrderReport;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnSendMail;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<CustomerTM, String> colCustomerId;

    @FXML
    private TableColumn<CustomerTM, String> colEmail;

    @FXML
    private TableColumn<CustomerTM, String> colName;

    @FXML
    private TableColumn<CustomerTM, String> colNic;

    @FXML
    private TableColumn<CustomerTM, String> colPhone;

    @FXML
    private TableView<CustomerTM> tblCustomer;

    @FXML
    private TextField txtCustomerID;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtNic;

    @FXML
    private TextField txtPhone;

    CustomerBO customerBO = (CustomerBO) BOFactory.getInstance().getBO(BOFactory.BOType.Customer);

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

    @FXML
    void imgResizeClick(MouseEvent event) {
        Stage stage = (Stage) imgResize.getScene().getWindow();
        stage.setFullScreen(true);


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        applyFadeAnimation(lblCustomer);

        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        try {
            refreshPage();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void refreshPage() throws SQLException, ClassNotFoundException {
        refreshTable();

        String nextCustomerID = customerBO.getLastPk().get(); //get nextId from model
        txtCustomerID.setText(nextCustomerID);
        txtCustomerID.setEditable(false);

        txtName.setText("");
        txtNic.setText("");
        txtEmail.setText("");
        txtPhone.setText("");

        btnSave.setDisable(false);
        btnGetAllReports.setDisable(false);

        btnSendMail.setDisable(true);
        btnDelete.setDisable(true);
        btnReset.setDisable(true);
        btnOrderReport.setDisable(true);
        btnUpdate.setDisable(true);
    }

    private void refreshTable() throws SQLException, ClassNotFoundException {
        ArrayList<CustomerDTO> customerDTOS = (ArrayList<CustomerDTO>) customerBO.getAll(); //model getAll

        ObservableList<CustomerTM> customerTMS = FXCollections.observableArrayList();
        
        for (CustomerDTO customerDTO : customerDTOS) {
            CustomerTM customerTM = new CustomerTM(
                    customerDTO.getId(),
                    customerDTO.getName(),
                    customerDTO.getNic(),
                    customerDTO.getEmail(),
                    customerDTO.getPhone()
            );
            customerTMS.add(customerTM);
        }
        tblCustomer.setItems(customerTMS);
    }

    @FXML
    void onHomeClick(MouseEvent event) throws IOException {
        URL resource = this.getClass().getResource("/view/dashboard.fxml");
        Parent root = FXMLLoader.load(resource);
        Scene scene = new Scene(root);
        Stage primaryStage = (Stage) (this.root.getScene().getWindow());
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        Platform.runLater(() -> primaryStage.sizeToScene());
    }

    @FXML
    void onDeleteClick(ActionEvent event) throws SQLException, ClassNotFoundException {
        System.out.println("customer delete clicked");
        String customerId = txtCustomerID.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this customer?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get() == ButtonType.YES) {

            boolean isDeleted = customerBO.deleteByPk(customerId);

            if (isDeleted) {
                new Alert(Alert.AlertType.INFORMATION, "Customer deleted...!").show();
                refreshPage();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to delete customer...!").show();
            }
        }

    }

    @FXML
    void onOrderReportClick(ActionEvent event) {
        System.out.println("customer report clicked");
        String selectedCustomerId = txtCustomerID.getText();

        try {
            Connection connection = DBConnection.getInstance().getConnection();

            Map<String, Object> parameters = new HashMap<>();

            parameters.put("P_Customer_Id", selectedCustomerId);

            JasperReport jasperReport = JasperCompileManager.compileReport(getClass().getResourceAsStream("/report/customer_order_report.jrxml"));

            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport,
                    parameters,
                    connection
            );

            JasperViewer.viewReport(jasperPrint, false);

        } catch (JRException e) {
            new Alert(Alert.AlertType.ERROR, "Fail to load report..!");
            e.printStackTrace();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Data empty..!");
            e.printStackTrace();
        }

    }

    @FXML
    void onReportClick(ActionEvent event) {
        System.out.println("report clicked");
        try {
            Connection connection = DBConnection.getInstance().getConnection();

            Map<String, Object> parameters = new HashMap<>();

            parameters.put("today", LocalDate.now().toString());
            parameters.put("TODAY", LocalDate.now().toString());

            JasperReport jasperReport = JasperCompileManager.compileReport(getClass().getResourceAsStream("/report/Blank_A4_4.jrxml"));

            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport,
                    parameters,
                    connection
            );

            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException e) {
            new Alert(Alert.AlertType.ERROR, "Fail to load report..!");
            e.printStackTrace();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Data empty..!");
            e.printStackTrace();
        }

    }

    @FXML
    void onResetClick(ActionEvent event) throws SQLException, ClassNotFoundException {
        System.out.println("customer reset clicked");
        refreshPage();
    }

    @FXML
    void onSaveClick(ActionEvent event) throws SQLException, ClassNotFoundException {
        System.out.println("customer save clicked");

        String id = txtCustomerID.getText();
        String name = txtName.getText();
        String nic = txtNic.getText();
        String email = txtEmail.getText();
        String phone = txtPhone.getText();

        CustomerDTO customerDTO = new CustomerDTO(id, name, nic, email, phone);

        boolean isSaved = customerBO.save(customerDTO);

        if (isSaved) {
            new Alert(Alert.AlertType.INFORMATION, "Customer saved...!").show();
            refreshPage();
        } else {
            new Alert(Alert.AlertType.ERROR, "Fail to save customer...!").show();
        }

    }

    @FXML
    void onSendMailClick(ActionEvent event) {
        System.out.println("customer send mail clicked");

    }

    @FXML
    void onUpdateClick(ActionEvent event) throws SQLException, ClassNotFoundException {
        System.out.println("customer update clicked");

        String id = txtCustomerID.getText();
        String name = txtName.getText();
        String nic = txtNic.getText();
        String email = txtEmail.getText();
        String phone = txtPhone.getText();

        CustomerDTO customerDTO = new CustomerDTO(id, name, nic, email, phone);

        boolean isUpdate = customerBO.update(customerDTO);

        if (isUpdate) {
            new Alert(Alert.AlertType.INFORMATION, "Customer updated...!").show();
            refreshPage();
        } else {
            new Alert(Alert.AlertType.ERROR, "Fail to update customer...!").show();
        }
    }


    @FXML
    void onClickTable(MouseEvent event) {
        CustomerTM selectedItem = tblCustomer.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            txtCustomerID.setText(selectedItem.getId());
            txtCustomerID.setEditable(false);
            txtName.setText(selectedItem.getName());
            txtNic.setText(selectedItem.getNic());
            txtEmail.setText(selectedItem.getEmail());
            txtPhone.setText(selectedItem.getPhone());

            btnSave.setDisable(true);

            btnDelete.setDisable(false);
            btnUpdate.setDisable(false);
            btnOrderReport.setDisable(false);
            btnSendMail.setDisable(false);
            btnReset.setDisable(false);
        }
    }
}
