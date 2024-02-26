package spring.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Order_generator")
    @SequenceGenerator(name="Order_generator", sequenceName = "Order_seq", allocationSize = 5)
    private Long id;

    @Column(unique = true,nullable = false)
    private Long order_number;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private Order.PaymentMethod payment_method;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private Order.PaymentStatus payment_status;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Order.Status status;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime order_date;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime possibly_ready_time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dopInfoCoffeeshop_id")
    private DopInfoCoffeeshop dopInfoCoffeeshop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

   @OneToMany(fetch = FetchType.LAZY,mappedBy = "order", cascade = CascadeType.ALL,orphanRemoval = true)
   private List<OrderItems> items = new ArrayList<>();

    @Column(nullable = false)
    private double total_price;

   public enum PaymentStatus {
       Paid,
       Unpaid,
       Refunded
   }

   public enum PaymentMethod {
       Card,
       Cash
   }

   public enum Status {
       Ready,
       Not_ready,
       Canceled
   }

   @PrePersist
   public void prePersist(){
        this.order_number = id + 100;
    }

}
