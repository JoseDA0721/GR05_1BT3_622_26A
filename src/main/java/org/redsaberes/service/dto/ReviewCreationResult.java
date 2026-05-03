package org.redsaberes.service.dto;

/**
 * DTO que encapsula el resultado de crear una reseña.
 */
public class ReviewCreationResult {
    private final ReviewCreationOutcome outcome;

    private ReviewCreationResult(ReviewCreationOutcome outcome) {
        this.outcome = outcome;
    }

    public ReviewCreationOutcome getOutcome() {
        return outcome;
    }

    public boolean isSuccess() {
        return outcome == ReviewCreationOutcome.SUCCESS;
    }

    public static ReviewCreationResult success() {
        return new ReviewCreationResult(ReviewCreationOutcome.SUCCESS);
    }

    public static ReviewCreationResult fromOutcome(ReviewCreationOutcome outcome) {
        return new ReviewCreationResult(outcome);
    }

    public static ReviewCreationResult invalidStars() {
        return new ReviewCreationResult(ReviewCreationOutcome.INVALID_STARS);
    }

    public static ReviewCreationResult commentTooLong() {
        return new ReviewCreationResult(ReviewCreationOutcome.COMMENT_TOO_LONG);
    }

    public static ReviewCreationResult commentOffensive() {
        return new ReviewCreationResult(ReviewCreationOutcome.COMMENT_OFFENSIVE);
    }

    public static ReviewCreationResult alreadyReviewed() {
        return new ReviewCreationResult(ReviewCreationOutcome.ALREADY_REVIEWED);
    }

    public static ReviewCreationResult saveError() {
        return new ReviewCreationResult(ReviewCreationOutcome.SAVE_ERROR);
    }

    public static ReviewCreationResult unauthorized() {
        return new ReviewCreationResult(ReviewCreationOutcome.UNAUTHORIZED);
    }
}

