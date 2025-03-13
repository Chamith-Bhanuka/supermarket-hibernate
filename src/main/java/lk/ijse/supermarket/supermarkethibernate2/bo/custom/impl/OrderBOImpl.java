package lk.ijse.supermarket.supermarkethibernate2.bo.custom.impl;

import lk.ijse.supermarket.supermarkethibernate2.bo.custom.OrderBO;
import lk.ijse.supermarket.supermarkethibernate2.dao.DAOFactory;
import lk.ijse.supermarket.supermarkethibernate2.dao.custom.OrderDAO;
import lk.ijse.supermarket.supermarkethibernate2.dto.ItemDTO;

import java.util.List;
import java.util.Optional;

public class OrderBOImpl implements OrderBO {
    OrderDAO dao = DAOFactory.getInstance().getDAO(DAOFactory.DAOType.Order);

    @Override
    public Optional<String> getLastPk() {
        Optional<String> optionalS = dao.getLastPk();

        String numericPart = optionalS.map(s -> s.substring(1)).orElse("0");

        int numericValue = Integer.parseInt(numericPart) + 1;

        String newValue = String.format("O%03d", numericValue);

        Optional<String> optionalNewValue = Optional.of(newValue);

        return optionalNewValue;
    }

    @Override
    public List<ItemDTO> getAll() {
        return List.of();
    }
}
