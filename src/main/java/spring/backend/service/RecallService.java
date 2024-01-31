package spring.backend.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import spring.backend.dto.RecallDTO;
import spring.backend.dto.RecallReactionDTO;
import spring.backend.entity.Recall;
import spring.backend.entity.RecallReaction;
import spring.backend.exception.AppRuntimeException;
import spring.backend.helpers.ReactionResult;
import spring.backend.repository.jpa.ReactionRepository;
import spring.backend.repository.jpa.RecallRepository;
import spring.backend.entity.User;
import spring.backend.entity.DopInfoCoffeeshop;

import java.util.Optional;

@Service
@AllArgsConstructor
public class RecallService {
    RecallRepository recallRepository;
    ReactionService reactionService;
    UserService userService;

    public Recall recallCoffeeShop(DopInfoCoffeeshop dopInfoCoffeeshop, User user, RecallDTO recall) {
        Optional<Recall> existingRateOptional = recallRepository.findByDopInfoCoffeeshopAndUser(dopInfoCoffeeshop, user);
        Recall existingRate = existingRateOptional.orElseGet(Recall::new);

        if (existingRate.getRate() == recall.getRating()) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "User set the same rating");
        } else {
            existingRate.setDopInfoCoffeeshop(dopInfoCoffeeshop);
            existingRate.setUser(user);
            existingRate.setText(recall.getText());
            existingRate.setRate(recall.getRating());
        }
        return recallRepository.save(existingRate);
    }

    @Transactional
    public void recallReaction(Long recall_id,RecallReactionDTO reaction) {
        Recall recall = recallRepository.findById(recall_id)
                .orElseThrow(() -> new EntityNotFoundException("DopInfo not found with id: " + recall_id));
        Hibernate.initialize(recall.getReactions());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            throw new AppRuntimeException("User is not authenticated", HttpStatus.UNAUTHORIZED);
        } else {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                ReactionResult result = reactionService.setReactions(recall,
                        userService.findByUsername(((UserDetails) principal).getUsername()),
                        reaction);

                if (result.isSaveRequired()) {
                    RecallReaction savedReaction = result.getRecallReaction();
                    recall.getReactions().add(savedReaction);
                } else {
                    recall.getReactions().remove(result.getRecallReaction());
                    reactionService.deleteReaction(result.getRecallReaction().getId());
                }
            }
        }
    }
}