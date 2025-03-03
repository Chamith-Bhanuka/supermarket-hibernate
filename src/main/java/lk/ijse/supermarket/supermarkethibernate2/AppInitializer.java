package lk.ijse.supermarket.supermarkethibernate2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lk.ijse.supermarket.supermarkethibernate2.config.FactoryConfiguration;
import lk.ijse.supermarket.supermarkethibernate2.dao.custom.impl.CustomerDAOImpl;
import lk.ijse.supermarket.supermarkethibernate2.entity.Customer;
import lk.ijse.supermarket.supermarkethibernate2.entity.Item;
import lk.ijse.supermarket.supermarkethibernate2.entity.SuperEntity;
import org.hibernate.Session;

import java.io.IOException;
import java.util.Optional;

public class AppInitializer extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        //https://github.com/shamodhas/supermarket-layered-orm-gdse-71/blob/master/src/main/java/lk/ijse/gdse/supermarket/AppInitializer.java

        FXMLLoader fxmlLoader = new FXMLLoader(AppInitializer.class.getResource("/view/dashboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    public static void main(String[] args) {
        Session session = FactoryConfiguration.getInstance().getSession();
        launch();
        session.close();

//        SuperEntity customer = new Customer();
//        SuperEntity item = new Item();
//        CustomerDAOImpl customerDAO = new CustomerDAOImpl();
//
//        Optional<Customer> optionalCustomer = customerDAO.findByPk("C008");
//
//        if (!optionalCustomer.isEmpty()){
//            Customer customer1 = optionalCustomer.get();
//        }
//
//        // optionalCustomer.isEmpty() - no data
//        // optionalCustomer.isPresent() - have data
//
//        if (optionalCustomer.isPresent()){
//            // have data
//            Customer customer1 = optionalCustomer.get();
//        }
//
//        Session session = FactoryConfiguration.getInstance().getSession();
//        session.close();
//
//        System.out.println("Get customer from ID: " + optionalCustomer.get().getName());

    }
}