package lk.ijse.supermarket.supermarkethibernate2.dao.custom;

import lk.ijse.supermarket.supermarkethibernate2.dao.CrudDAO;
import lk.ijse.supermarket.supermarkethibernate2.entity.Order;
import org.hibernate.Session;

public interface OrderDAO extends CrudDAO<Order, String> {
    boolean saveOrderWIthOrderDetails(Session session, Order order);
}
