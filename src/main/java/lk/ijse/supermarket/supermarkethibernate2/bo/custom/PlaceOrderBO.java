package lk.ijse.supermarket.supermarkethibernate2.bo.custom;

import lk.ijse.supermarket.supermarkethibernate2.bo.SuperBO;
import lk.ijse.supermarket.supermarkethibernate2.dto.OrderDTO;

public interface PlaceOrderBO extends SuperBO {
    boolean placeOrder(OrderDTO orderDTO);
}
