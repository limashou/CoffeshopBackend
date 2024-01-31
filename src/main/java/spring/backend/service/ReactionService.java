package spring.backend.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import spring.backend.dto.RecallReactionDTO;
import spring.backend.entity.Recall;
import spring.backend.entity.RecallReaction;
import spring.backend.entity.User;
import spring.backend.helpers.ReactionResult;
import spring.backend.repository.jpa.ReactionRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ReactionService {
    ReactionRepository reactionRepository;

    public void deleteReaction(Long id) {
        try {
            reactionRepository.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting RecallReaction with ID: " + id, e);
        }
    }
    public ReactionResult setReactions(Recall recall, User user, RecallReactionDTO reaction) {
        Optional<RecallReaction> existingRateOptional = reactionRepository.findByRecallAndUser(recall, user);
        if (existingRateOptional.isPresent()) {
            RecallReaction existingRate = existingRateOptional.get();
            if (existingRate.getReactionType().equals(RecallReaction.ReactionType.valueOf(reaction.getReaction()))) {
                System.out.println("Same reaction. Deleting existing reaction.");
                return new ReactionResult(false, existingRate);
            } else {
                System.out.println("Another reaction. Updating existing reaction.");
                existingRate.setReactionType(RecallReaction.ReactionType.valueOf(reaction.getReaction()));
                return new ReactionResult(true, reactionRepository.save(existingRate));
            }
        } else {
            System.out.println("New reaction. Creating a new reaction.");
            RecallReaction newRate = new RecallReaction();
            newRate.setUser(user);
            newRate.setRecall(recall);
            newRate.setReactionType(RecallReaction.ReactionType.valueOf(reaction.getReaction()));
            return new ReactionResult(true, reactionRepository.save(newRate));
        }
    }
}
