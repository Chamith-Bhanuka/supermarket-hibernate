package lk.ijse.supermarket.supermarkethibernate2.dao;

import lk.ijse.supermarket.supermarkethibernate2.dao.custom.impl.CustomerDAOImpl;
import lk.ijse.supermarket.supermarkethibernate2.dao.custom.impl.ItemDAOImpl;
import lombok.NoArgsConstructor;

@NoArgsConstructor

public class DAOFactory {
    private static DAOFactory daoFactory;

    public static DAOFactory getInstance() {
        return daoFactory==null?daoFactory=new DAOFactory():daoFactory;
    }

    public enum DAOType {
        Customer, Item
    }

    public SuperDAO getDAO(DAOType type) {
        return switch (type) {
            case Customer -> new CustomerDAOImpl();
            case Item -> new ItemDAOImpl();
        };
    }
}
