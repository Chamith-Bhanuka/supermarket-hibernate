package lk.ijse.supermarket.supermarkethibernate2.bo.custom;

import lk.ijse.supermarket.supermarkethibernate2.bo.SuperBO;
import lk.ijse.supermarket.supermarkethibernate2.dto.ItemDTO;

import java.util.List;
import java.util.Optional;

public interface OrderBO extends SuperBO {
    Optional<String> getLastPk();
    List<ItemDTO> getAll();
}
