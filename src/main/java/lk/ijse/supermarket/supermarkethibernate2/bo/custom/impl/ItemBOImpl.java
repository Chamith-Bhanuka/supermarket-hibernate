package lk.ijse.supermarket.supermarkethibernate2.bo.custom.impl;

import lk.ijse.supermarket.supermarkethibernate2.bo.custom.ItemBO;
import lk.ijse.supermarket.supermarkethibernate2.dao.DAOFactory;
import lk.ijse.supermarket.supermarkethibernate2.dao.custom.ItemDAO;
import lk.ijse.supermarket.supermarkethibernate2.dto.ItemDTO;
import lk.ijse.supermarket.supermarkethibernate2.entity.Item;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemBOImpl implements ItemBO {

    ItemDAO dao = (ItemDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.Item);

    @Override
    public boolean save(ItemDTO itemDTO) {
        Item item = new Item();

        item.setId(itemDTO.getItemId());
        item.setName(itemDTO.getItemName());
        item.setQuantity(itemDTO.getQuantity());
        item.setUnitPrice(BigDecimal.valueOf(itemDTO.getPrice()));

        return dao.save(item);
    }

    @Override
    public boolean update(ItemDTO itemDTO) {
        Item item = new Item();

        item.setId(itemDTO.getItemId());
        item.setName(itemDTO.getItemName());
        item.setQuantity(itemDTO.getQuantity());
        item.setUnitPrice(BigDecimal.valueOf(itemDTO.getPrice()));

        return dao.update(item);
    }

    @Override
    public boolean deleteByPk(String pk) {
        return dao.deleteByPk(pk);
    }

    @Override
    public List<ItemDTO> getAll() {
        List<ItemDTO> dtoList = new ArrayList<>();
        List<Item> entityList = dao.getAll();

        for (Item item : entityList) {
            dtoList.add(new ItemDTO(
                    item.getId(),
                    item.getName(),
                    item.getQuantity(),
                    item.getUnitPrice().doubleValue()
            ));
        }
        return dtoList;

    }

    @Override
    public Optional<ItemDTO> findByPk(String pk) {
        return Optional.empty();
    }

    @Override
    public Optional<String> getLastPk() {
        Optional<String> optionalS = dao.getLastPk();

        String numericPart = optionalS.map(s -> s.substring(1)).orElse("0");

        int numericValue = Integer.parseInt(numericPart) + 1;

        String newValue = String.format("I%03d", numericValue);

        Optional<String> optionalNewValue = Optional.of(newValue);

        return optionalNewValue;
    }
}
