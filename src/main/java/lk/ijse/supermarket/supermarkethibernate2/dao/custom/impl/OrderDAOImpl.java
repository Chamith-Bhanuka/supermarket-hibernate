package lk.ijse.supermarket.supermarkethibernate2.dao.custom.impl;

import lk.ijse.supermarket.supermarkethibernate2.config.FactoryConfiguration;
import lk.ijse.supermarket.supermarkethibernate2.dao.custom.OrderDAO;
import lk.ijse.supermarket.supermarkethibernate2.entity.Order;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class OrderDAOImpl implements OrderDAO {

    private final FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();

    @Override
    public boolean save(Order order) {
        return false;
    }

    @Override
    public boolean update(Order order) {
        return false;
    }

    @Override
    public boolean deleteByPk(String pk) {
        return false;
    }

    @Override
    public List<Order> getAll() {
        return List.of();
    }

    @Override
    public Optional<Order> findByPk(String pk) {
        Session session = factoryConfiguration.getSession();
        Order order = session.get(Order.class, pk);
        return Optional.ofNullable(order);
    }

    @Override
    public Optional<String> getLastPk() {
        Session session = factoryConfiguration.getSession();

        // select order_id from orders order by customer_id desc limit 1
        String lastPk = session
                .createQuery("SELECT o.id FROM Order o ORDER BY o.id DESC", String.class)
                .setMaxResults(1)
                .uniqueResult();

        return Optional.ofNullable(lastPk);
    }

    @Override
    public boolean saveOrderWIthOrderDetails(Session session, Order order) {
        try {
            session.merge(order);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
