package org.redsaberes.service.dto;

/**
 * Outcomes posibles al crear una reseña.
 */
public enum ReviewCreationOutcome {
    SUCCESS("review-saved"),
    INVALID_STARS("invalid-stars"),
    COMMENT_TOO_LONG("comment-too-long"),
    COMMENT_OFFENSIVE("comment-offensive"),
    ALREADY_REVIEWED("already-reviewed"),
    SAVE_ERROR("save-error"),
    UNAUTHORIZED("access-denied");

    private final String msgKey;

    ReviewCreationOutcome(String msgKey) {
        this.msgKey = msgKey;
    }

    public String getMsgKey() {
        return msgKey;
    }

    public static ReviewCreationOutcome fromMsgKey(String msgKey) {
        if (msgKey == null) {
            return null;
        }
        for (ReviewCreationOutcome outcome : values()) {
            if (outcome.msgKey.equals(msgKey)) {
                return outcome;
            }
        }
        return null;
    }
}

