package spring.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "recall_reaction")
public class RecallReaction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RecallReaction_generator")
    @SequenceGenerator(name="RecallReaction_generator", sequenceName = "RecallReaction_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recall_id")
    private Recall recall;

    @Enumerated(EnumType.STRING)
    private ReactionType reactionType;
    public enum ReactionType {
        LIKE,
        DISLIKE
    }
}
