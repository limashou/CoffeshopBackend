package spring.backend.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import spring.backend.dto.recall.RecallReactionDTO;
import spring.backend.entity.Recall;
import spring.backend.entity.RecallReaction;
import spring.backend.entity.User;
import spring.backend.exception.AppRuntimeException;
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
        } catch (Exception error) {
            throw new AppRuntimeException("Error deleting RecallReaction with ID: " + id, HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    public ReactionResult setReactions(Recall recall, User user, RecallReactionDTO reaction) {
        Optional<RecallReaction> existingRateOptional = reactionRepository.findByRecallAndUser(recall, user);
        if (existingRateOptional.isPresent()) {
            RecallReaction existingRate = existingRateOptional.get();
            if (existingRate.getReactionType().equals(RecallReaction.ReactionType.valueOf(reaction.getReaction()))) {
                return new ReactionResult(false, existingRate);
            } else {
                existingRate.setReactionType(RecallReaction.ReactionType.valueOf(reaction.getReaction()));
                return new ReactionResult(true, reactionRepository.save(existingRate));
            }
        } else {
            RecallReaction newRate = new RecallReaction();
            newRate.setUser(user);
            newRate.setRecall(recall);
            newRate.setReactionType(RecallReaction.ReactionType.valueOf(reaction.getReaction()));
            return new ReactionResult(true, reactionRepository.save(newRate));
        }
    }
}
