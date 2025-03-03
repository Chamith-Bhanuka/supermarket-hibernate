package lk.ijse.supermarket.supermarkethibernate2.dao;

import lk.ijse.supermarket.supermarkethibernate2.entity.SuperEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface CrudDAO<T extends SuperEntity,ID> extends SuperDAO {
    boolean save(T t);
    boolean update(T t);
    boolean deleteByPk(ID pk);
    List<T> getAll();
    Optional<T> findByPk(ID pk);
    Optional<String> getLastPk();

}
