package spring.backend.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.backend.entity.Recall;
import spring.backend.entity.RecallReaction;
import spring.backend.entity.User;

import java.util.Optional;

public interface ReactionRepository extends JpaRepository<RecallReaction,Long> {
    Optional<RecallReaction> findByRecallAndUser(Recall recall, User user);

}
