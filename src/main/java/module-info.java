module lk.ijse.supermarket.supermarkethibernate2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires net.sf.jasperreports.core;

    opens lk.ijse.supermarket.supermarkethibernate2.controller to javafx.fxml;
    opens lk.ijse.supermarket.supermarkethibernate2.entity to org.hibernate.orm.core;
    opens lk.ijse.supermarket.supermarkethibernate2.view.tdm to javafx.base;
    opens lk.ijse.supermarket.supermarkethibernate2.config to jakarta.persistence;
    exports lk.ijse.supermarket.supermarkethibernate2;
    exports lk.ijse.supermarket.supermarkethibernate2.query;
}