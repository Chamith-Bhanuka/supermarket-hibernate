package lk.ijse.supermarket.supermarkethibernate2.bo.custom;

import lk.ijse.supermarket.supermarkethibernate2.bo.SuperBO;
import lk.ijse.supermarket.supermarkethibernate2.dto.CustomerDTO;
import java.util.List;
import java.util.Optional;

public interface CustomerBO extends SuperBO {
    boolean save(CustomerDTO customerDTO);
    boolean update(CustomerDTO customerDTO);
    boolean deleteByPk(String pk);
    List<CustomerDTO> getAll();
    Optional<CustomerDTO> findByPk(String pk);
    Optional<String> getLastPk();

}
