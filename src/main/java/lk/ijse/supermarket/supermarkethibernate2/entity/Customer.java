package lk.ijse.supermarket.supermarkethibernate2.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
//@Data
@Getter
@Setter
@Entity
@Table(name="customer")
//inverse side (Order - Customer)

public class Customer implements SuperEntity{
    @Id
    @Column(name = "customer_id")
    private String id;

    private String name;
    private String nic;
    private String email;
    private String phone;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Order> orders;
}
