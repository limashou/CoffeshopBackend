package spring.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "order_items")
public class OrderItems {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Order_items_generator")
    @SequenceGenerator(name="Order_items_generator", sequenceName = "Order_items_seq", allocationSize = 5)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "items_id")
    private Long item;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_type")
    private OrderItems.ItemType itemType;

    @Column(name = "quantity")
    private Integer quantity;

    public enum ItemType {
        MENU,
        SPECIAL_OFFER
    }
}
