package lk.ijse.supermarket.supermarkethibernate2.bo.custom.impl;

import lk.ijse.supermarket.supermarkethibernate2.bo.custom.CustomerBO;
import lk.ijse.supermarket.supermarkethibernate2.dao.DAOFactory;
import lk.ijse.supermarket.supermarkethibernate2.dao.custom.CustomerDAO;
import lk.ijse.supermarket.supermarkethibernate2.dto.CustomerDTO;
import lk.ijse.supermarket.supermarkethibernate2.entity.Customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerBOImpl implements CustomerBO {

    CustomerDAO dao = DAOFactory.getInstance().getDAO(DAOFactory.DAOType.Customer);

    @Override
    public boolean save(CustomerDTO customerDTO) {
        Customer customer = new Customer();

        customer.setId(customerDTO.getId());
        customer.setName(customerDTO.getName());
        customer.setNic(customerDTO.getNic());
        customer.setEmail(customerDTO.getEmail());
        customer.setPhone(customerDTO.getPhone());

        return dao.save(customer);
    }

    @Override
    public boolean update(CustomerDTO customerDTO) {
        Customer customer = new Customer();

        customer.setId(customerDTO.getId());
        customer.setName(customerDTO.getName());
        customer.setNic(customerDTO.getNic());
        customer.setEmail(customerDTO.getEmail());
        customer.setPhone(customerDTO.getPhone());

        return dao.update(customer);
    }

    @Override
    public boolean deleteByPk(String pk) {
        return dao.deleteByPk(pk);
    }

    @Override
    public List<CustomerDTO> getAll() {
        ArrayList<CustomerDTO> customerDTOArrayList = new ArrayList<>();
        List<Customer> entityList = dao.getAll();

        for (Customer customer : entityList) {
            customerDTOArrayList.add(new CustomerDTO(
                    customer.getId(),
                    customer.getName(),
                    customer.getNic(),
                    customer.getEmail(),
                    customer.getPhone()
            ));
        }
        return customerDTOArrayList;
    }

    @Override
    public Optional<CustomerDTO> findByPk(String pk) {

        CustomerDTO customerDTO = new CustomerDTO();

        Optional<Customer> entityOptional = dao.findByPk(pk);

        if (entityOptional.isPresent()) {
            customerDTO.setId(entityOptional.get().getId());
            customerDTO.setName(entityOptional.get().getName());
            customerDTO.setNic(entityOptional.get().getNic());
            customerDTO.setEmail(entityOptional.get().getEmail());
            customerDTO.setPhone(entityOptional.get().getPhone());
        }
        return Optional.of(customerDTO);

    }

    @Override
    public Optional<String> getLastPk() {
//        return dao.getLastPk();
        Optional<String> optionalS = dao.getLastPk();

        String numericPart = optionalS.map(s -> s.substring(1)).orElse("0");

        int numericValue = Integer.parseInt(numericPart) + 1;

        String newValue = String.format("C%03d", numericValue);

        Optional<String> optionalNewValue = Optional.of(newValue);

        return optionalNewValue;
    }

    @Override
    public ArrayList<String> getAllCustomerIds() {
        return dao.getAllCustomerIds();
    }
}
