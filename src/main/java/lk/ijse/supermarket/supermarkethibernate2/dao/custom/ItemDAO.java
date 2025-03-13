package lk.ijse.supermarket.supermarkethibernate2.dao.custom;

import lk.ijse.supermarket.supermarkethibernate2.dao.CrudDAO;
import lk.ijse.supermarket.supermarkethibernate2.entity.Item;
import org.hibernate.Session;

import java.util.ArrayList;

public interface ItemDAO extends CrudDAO<Item,String> {
    ArrayList<String> getAllItemIds();
    boolean updateItemWithOrder(Session session, Item item);
}
