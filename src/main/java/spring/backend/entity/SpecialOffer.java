package spring.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "special_offer")
public class SpecialOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true,length = 45,nullable = false)
    private String offerName;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "coffeeshop_id")
    private Coffeeshop coffeeshop;

    @ToString.Exclude
//    @OneToMany(fetch = FetchType.LAZY)
//    @JoinTable(
//            name = "offer_items",
//            joinColumns = @JoinColumn(name = "offer_id"),
//            inverseJoinColumns = @JoinColumn(name = "menu_id")
//    )
//    private List<Menu> itemsList;
    @ElementCollection
    @CollectionTable(
            name = "offer_items",
            joinColumns = @JoinColumn(name = "offer_id")
    )
    @MapKeyJoinColumn(name = "items_id")
    @Column(name = "quanity")
    private Map<Menu,Integer> itemsList;

    private double price;
}
