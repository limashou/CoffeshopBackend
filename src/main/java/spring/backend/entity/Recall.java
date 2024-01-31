package spring.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "recall")
public class Recall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dopInfoCoffeeshop_id")
    private DopInfoCoffeeshop dopInfoCoffeeshop;

    @Column(columnDefinition = "TEXT")
    private String text;

    @Column
    private int rate;

    @OneToMany(mappedBy = "recall", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RecallReaction> reactions = new ArrayList<>();

//    public void addReaction(RecallReaction reaction) {
//        reactions.add(reaction);
//        reaction.setRecall(this);
//    }
//
//    public void removeReaction(RecallReaction reaction) {
//        if (reactions != null) {
//            reactions.remove(reaction);
//            reaction.setRecall(null);
//        }
//    }
}
