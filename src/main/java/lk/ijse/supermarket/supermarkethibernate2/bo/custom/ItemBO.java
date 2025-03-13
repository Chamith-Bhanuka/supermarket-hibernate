package lk.ijse.supermarket.supermarkethibernate2.bo.custom;

import lk.ijse.supermarket.supermarkethibernate2.bo.SuperBO;
import lk.ijse.supermarket.supermarkethibernate2.dao.custom.ItemDAO;
import lk.ijse.supermarket.supermarkethibernate2.dto.CustomerDTO;
import lk.ijse.supermarket.supermarkethibernate2.dto.ItemDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface ItemBO extends SuperBO {
    boolean save(ItemDTO itemDTO);
    boolean update(ItemDTO itemDTO);
    boolean deleteByPk(String pk);
    List<ItemDTO> getAll();
    Optional<ItemDTO> findByPk(String pk);
    Optional<String> getLastPk();
    ArrayList<String> getAllItemIds();
}
