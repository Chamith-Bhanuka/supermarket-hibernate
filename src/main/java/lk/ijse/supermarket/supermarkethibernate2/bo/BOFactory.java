package lk.ijse.supermarket.supermarkethibernate2.bo;

import lk.ijse.supermarket.supermarkethibernate2.bo.custom.impl.CustomerBOImpl;
import lk.ijse.supermarket.supermarkethibernate2.bo.custom.impl.ItemBOImpl;
import lombok.NoArgsConstructor;

@NoArgsConstructor

public class BOFactory {

    private static BOFactory boFactory;

    public static BOFactory getInstance() {
        return boFactory==null?boFactory=new BOFactory():boFactory;
    }

    public enum BOType {
        Customer, Item
    }

    public SuperBO getBO(BOFactory.BOType type) {
        return switch (type) {
            case Customer -> new CustomerBOImpl();
            case Item -> new ItemBOImpl();
        };
    }
}
