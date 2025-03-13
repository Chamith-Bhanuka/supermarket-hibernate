package lk.ijse.supermarket.supermarkethibernate2.dao.custom;

import lk.ijse.supermarket.supermarkethibernate2.dao.CrudDAO;
import lk.ijse.supermarket.supermarkethibernate2.entity.Customer;

import java.util.ArrayList;

public interface CustomerDAO extends CrudDAO<Customer,String> {

    ArrayList<String> getAllCustomerIds();

}
