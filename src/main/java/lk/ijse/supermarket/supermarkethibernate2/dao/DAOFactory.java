package lk.ijse.supermarket.supermarkethibernate2.dao;

import lk.ijse.supermarket.supermarkethibernate2.dao.custom.impl.CustomerDAOImpl;
import lk.ijse.supermarket.supermarkethibernate2.dao.custom.impl.ItemDAOImpl;
import lk.ijse.supermarket.supermarkethibernate2.dao.custom.impl.OrderDAOImpl;
import lombok.NoArgsConstructor;

@NoArgsConstructor

public class DAOFactory {
    private static DAOFactory daoFactory;

    public static DAOFactory getInstance() {
        return daoFactory==null?daoFactory=new DAOFactory():daoFactory;
    }

    public enum DAOType {
        Customer, Item, Order
    }

    @SuppressWarnings("unchecked")
    public <T extends SuperDAO>T getDAO(DAOType type) {
        return switch (type) {
            case Customer -> (T) new CustomerDAOImpl();
            case Item ->(T) new ItemDAOImpl();
            case Order -> (T) new OrderDAOImpl();
        };
    }
}
