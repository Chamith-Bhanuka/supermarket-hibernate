package lk.ijse.supermarket.supermarkethibernate2.bo.custom.impl;

import lk.ijse.supermarket.supermarkethibernate2.bo.custom.PlaceOrderBO;
import lk.ijse.supermarket.supermarkethibernate2.config.FactoryConfiguration;
import lk.ijse.supermarket.supermarkethibernate2.dao.DAOFactory;
import lk.ijse.supermarket.supermarkethibernate2.dao.custom.CustomerDAO;
import lk.ijse.supermarket.supermarkethibernate2.dao.custom.ItemDAO;
import lk.ijse.supermarket.supermarkethibernate2.dao.custom.OrderDAO;
import lk.ijse.supermarket.supermarkethibernate2.dto.OrderDTO;
import lk.ijse.supermarket.supermarkethibernate2.dto.OrderDetailsDTO;
import lk.ijse.supermarket.supermarkethibernate2.entity.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlaceOrderBOImpl implements PlaceOrderBO {

    OrderDAO orderDAO = DAOFactory.getInstance().getDAO(DAOFactory.DAOType.Order);
    CustomerDAO customerDAO = DAOFactory.getInstance().getDAO(DAOFactory.DAOType.Customer);
    ItemDAO itemDAO = DAOFactory.getInstance().getDAO(DAOFactory.DAOType.Item);

    @Override
    public boolean placeOrder(OrderDTO orderDTO) {
        //transaction
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {
            String orderId = orderDTO.getOrderId();

            //Check order id exist
            Optional<Order> optionalOrder = orderDAO.findByPk(orderId);

            if (optionalOrder.isPresent()) {
                transaction.rollback();
                return false;
            }

            String customerId = orderDTO.getCustomerId();
            Optional<Customer> optionalCustomer = customerDAO.findByPk(customerId);

            if (optionalCustomer.isEmpty()) {
                transaction.rollback();
                return false;
            }

            Customer customer = optionalCustomer.get();

            Order order = new Order();
            order.setId(orderId);
            order.setDate(orderDTO.getOrderDate());
            order.setCustomer(customer);

            List<OrderDetails> orderDetailsList = new ArrayList<>();

            ArrayList<OrderDetailsDTO> orderDetailsDTOS = orderDTO.getOrderDetailsDTOS();

            for (OrderDetailsDTO orderDetailsDTO : orderDetailsDTOS) {
                String itemId = orderDetailsDTO.getItemId();
                Optional<Item> optionalItem = itemDAO.findByPk(itemId);

                if (optionalItem.isEmpty()) {
                    transaction.rollback();
                    return false;
                }

                Item item = optionalItem.get();

                OrderDetailsId orderDetailsId = new OrderDetailsId(orderId, itemId);

                OrderDetails orderDetails = new OrderDetails();
                orderDetails.setId(orderDetailsId);
                orderDetails.setOrder(order);
                orderDetails.setItem(item);
                orderDetails.setQty(orderDetailsDTO.getQuantity());
                orderDetails.setUnitPrice(BigDecimal.valueOf(orderDetailsDTO.getPrice()));

                orderDetailsList.add(orderDetails);
            }

            order.setOrderDetails(orderDetailsList);

            boolean isOrderSaved =orderDAO.saveOrderWIthOrderDetails(session, order);

            if (!isOrderSaved) {
                transaction.rollback();
                return false;
            }

            for (OrderDetails orderDetails : orderDetailsList) {
                String itemId = orderDetails.getItem().getId();
                Optional<Item> optionalItem = itemDAO.findByPk(itemId);

                if (optionalItem.isEmpty()) {
                    transaction.rollback();
                    return false;
                }

                Item item = optionalItem.get();

                if (item.getQuantity() < orderDetails.getQty()) {
                    transaction.rollback();
                    return false;
                }

                item.setQuantity(item.getQuantity() - orderDetails.getQty());

                boolean isItemUpdated = itemDAO.updateItemWithOrder(session, item);

                if (!isItemUpdated) {
                    transaction.rollback();
                    return false;
                }
            }

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
}
