package lk.ijse.supermarket.supermarkethibernate2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * --------------------------------------------
 * Author: Shamodha Sahan
 * GitHub: https://github.com/shamodhas
 * Website: https://shamodha.live
 * --------------------------------------------
 * Created: 2/20/2025 3:13 PM
 * Project: Relationship
 * --------------------------------------------
 **/

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "order_details")
public class OrderDetails implements SuperEntity {
//    @Id
//    private int id;
    @EmbeddedId
    private OrderDetailsId id;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @MapsId("itemId")
    @JoinColumn(name = "item_id")
    private Item item;

    private int qty;

    @Column(name = "unit_price", precision = 10, scale = 2)
    private BigDecimal unitPrice;
}

// O001 - I001
// O001 - I002
// O001 - I003

// O002 - I001
// O002 - I003
