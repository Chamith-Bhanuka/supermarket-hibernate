package lk.ijse.supermarket.supermarkethibernate2.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * --------------------------------------------
 * Author: Shamodha Sahan
 * GitHub: https://github.com/shamodhas
 * Website: https://shamodha.live
 * --------------------------------------------
 * Created: 2/20/2025 3:21 PM
 * Project: Relationship
 * --------------------------------------------
 **/

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class OrderDetailsId implements SuperEntity {
//    @Column(name = "order_id")
    private String orderId;

//    @Column(name = "item_id")
    private String itemId;
}
