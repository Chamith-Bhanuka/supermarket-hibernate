package lk.ijse.supermarket.supermarkethibernate2.dao.custom.impl;

import lk.ijse.supermarket.supermarkethibernate2.config.FactoryConfiguration;
import lk.ijse.supermarket.supermarkethibernate2.dao.custom.CustomerDAO;
import lk.ijse.supermarket.supermarkethibernate2.entity.Customer;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerDAOImpl implements CustomerDAO {

    private final FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();

    @Override
    public boolean save(Customer customer) {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();

        try {
            session.persist(customer);
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public boolean update(Customer customer) {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();

        try {
            session.merge(customer);
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public boolean deleteByPk(String pk) {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();

        try {
            Customer customer = session.get(Customer.class, pk);
            if (customer == null) {
                return false;
            }
            session.remove(customer);
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Customer> getAll() {
        Session session = factoryConfiguration.getSession();
        Query query = session.createQuery("from Customer", Customer.class);
        List<Customer> list = query.list();
        return list;
    }

    @Override
    public Optional<Customer> findByPk(String pk) {
        Session session = factoryConfiguration.getSession();
        Customer customer = session.get(Customer.class, pk);
        session.close();

        if (customer == null) {
            return Optional.empty();
        }
        return Optional.of(customer);
    }

    @Override
    public Optional<String> getLastPk() {
        Session session = factoryConfiguration.getSession();

        // select customer_id from customer order by customer_id desc limit 1
        String lastPk = session
                .createQuery("SELECT c.id FROM Customer c ORDER BY c.id DESC", String.class)
                .setMaxResults(1)
                .uniqueResult();

        return Optional.ofNullable(lastPk);
    }

    @Override
    public ArrayList<String> getAllCustomerIds() {
        Session session = FactoryConfiguration.getInstance().getSession();

        //get all Ids
        Query<String> query = session.createQuery("SELECT c.id FROM Customer c", String.class);
        List<String> customersIds = query.list();

        ArrayList<String> customerIds = new ArrayList<>(customersIds);

        session.close();

        return customerIds;

    }
}
