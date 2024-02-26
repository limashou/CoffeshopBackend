package spring.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "dop_info_coffeeshop")
public class DopInfoCoffeeshop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coffeeshop_id")
    private Coffeeshop coffeeshop;

    @Column(nullable = false,length = 30)
    private String city;

    @Column(nullable = false,length = 60)
    private String address;

    @ToString.Exclude
    @OneToMany(mappedBy = "dopInfoCoffeeshop", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    List<Recall> recallList = new ArrayList<>();

    @Transient
    private double averageRating;

    @ToString.Exclude
    @OneToMany(mappedBy = "dopInfoCoffeeshop",cascade = CascadeType.ALL,fetch = FetchType.LAZY, orphanRemoval = true)
    List<Order> orders = new ArrayList<>();

    public double getAverageRating() {
        return recallList.stream()
                .mapToDouble(Recall::getRate)
                .average()
                .orElse(0.0);
    }
}
