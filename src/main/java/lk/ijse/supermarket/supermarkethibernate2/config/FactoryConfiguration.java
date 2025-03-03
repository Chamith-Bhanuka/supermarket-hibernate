package lk.ijse.supermarket.supermarkethibernate2.config;


import lk.ijse.supermarket.supermarkethibernate2.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class FactoryConfiguration {
    private static FactoryConfiguration instance;
    private SessionFactory sessionFactory;
    private FactoryConfiguration() {
        Configuration config = new Configuration().configure();

        config.addAnnotatedClass(Customer.class);
        config.addAnnotatedClass(Order.class);
        config.addAnnotatedClass(Item.class);
        config.addAnnotatedClass(OrderDetails.class);
        config.addAnnotatedClass(OrderDetailsId.class);

        sessionFactory = config.buildSessionFactory();

    }
    public static FactoryConfiguration getInstance() {
        return (instance == null) ? instance = new FactoryConfiguration() : instance;
    }

    public Session getSession() {
        return sessionFactory.openSession();
    }
}
