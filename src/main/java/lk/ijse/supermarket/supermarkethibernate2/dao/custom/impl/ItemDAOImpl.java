package lk.ijse.supermarket.supermarkethibernate2.dao.custom.impl;

import lk.ijse.supermarket.supermarkethibernate2.config.FactoryConfiguration;
import lk.ijse.supermarket.supermarkethibernate2.dao.custom.ItemDAO;
import lk.ijse.supermarket.supermarkethibernate2.entity.Item;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemDAOImpl implements ItemDAO {

    private final FactoryConfiguration factoryConfiguration = FactoryConfiguration.getInstance();

    @Override
    public boolean save(Item item) {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();

        try {
            session.persist(item);
            transaction.commit();
            return true;
        }  catch (Exception e) {
            transaction.rollback();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean update(Item item) {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();

        try {
            session.merge(item);
            transaction.commit();
            return true;
        }  catch (Exception e) {
            transaction.rollback();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean deleteByPk(String pk) {
        Session session = factoryConfiguration.getSession();
        Transaction transaction = session.beginTransaction();

        try {
            Item item = session.get(Item.class, pk);
            if (item == null) {
                return false;
            }
            session.remove(item);
            transaction.commit();
            return true;
        }  catch (Exception e) {
            transaction.rollback();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public List<Item> getAll() {
        Session session = factoryConfiguration.getSession();
        Query query = session.createQuery("from Item", Item.class);
        List<Item> items = query.list();
        return items;
    }

    @Override
    public Optional<Item> findByPk(String pk) {
        Session session = factoryConfiguration.getSession();
        Item item = session.get(Item.class, pk);
        session.close();

        if (item == null) {
            return Optional.empty();
        }
        return Optional.of(item);
    }

    @Override
    public Optional<String> getLastPk() {
        Session session = factoryConfiguration.getSession();

        // select item_id from customer item by item_id desc limit 1
        String lastPk = session
                .createQuery("SELECT i.id FROM Item i ORDER BY i.id DESC", String.class)
                .setMaxResults(1)
                .uniqueResult();

        return Optional.ofNullable(lastPk);
    }

    @Override
    public ArrayList<String> getAllItemIds() {
        Session session = FactoryConfiguration.getInstance().getSession();

        //get all Ids
        Query<String> query = session.createQuery("SELECT i.id FROM Item i", String.class);
        List<String> itemIds = query.list();

        ArrayList<String> itemIdsArrayList = new ArrayList<>(itemIds);

        session.close();

        return itemIdsArrayList;
    }

    @Override
    public boolean updateItemWithOrder(Session session, Item item) {
        try {
            session.merge(item);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
