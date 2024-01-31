package spring.backend.helpers;

import lombok.Getter;
import spring.backend.entity.RecallReaction;

@Getter
public class ReactionResult {

    private final boolean saveRequired;
    private final RecallReaction recallReaction;

    public ReactionResult(boolean saveRequired, RecallReaction recallReaction) {
        this.saveRequired = saveRequired;
        this.recallReaction = recallReaction;
    }

}


