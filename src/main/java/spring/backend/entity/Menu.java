package spring.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "menu")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 35)
    private String items;

    @Column
    private double price;

    @ManyToOne
    @JoinColumn(name = "coffeeshop_id")
    private Coffeeshop coffeeshop;

    @Enumerated(EnumType.STRING)
    private Menu.ItemsType itemsType;
    public enum ItemsType {
        DRINK,
        MEAL
    }
}
