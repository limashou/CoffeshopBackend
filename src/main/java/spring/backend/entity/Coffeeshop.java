package spring.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "coffeeshop")
public class Coffeeshop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 45)
    private String name;
    @Column(length = 55)
    private String email;
    @Column
    private String description;

    @ToString.Exclude
    @OneToMany(mappedBy = "coffeeshop",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    List<DopInfoCoffeeshop> dopInfoCoffeeshops = new LinkedList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "coffeeshop",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    List<Menu> menuList = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "coffeeshop",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    List<SpecialOffer>  specialOfferList = new ArrayList<>();
}
