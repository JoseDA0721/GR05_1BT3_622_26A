package org.redsaberes.service.impl;

import org.redsaberes.repository.ReviewRepository;
import org.redsaberes.repository.impl.ReviewRepositoryImpl;
import org.redsaberes.service.ReviewService;

import java.text.Normalizer;
import java.util.Locale;
import java.util.Set;

public class ReviewServiceImpl implements ReviewService {
    private static final int MAX_COMMENT_LENGTH = 255;
    private static final Set<String> OFFENSIVE_WORDS = Set.of(
            "insulto1",
            "spam_link",
            "puto",
            "cabron",
            "tonto",
            "idiota",
            "estupido",
            "imbecil",
            "payaso",
            "inutil",
            "basura",
            "mentiroso",
            "tramposo"
    );
    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl() {
        this(new ReviewRepositoryImpl());
    }

    ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public void validateCommentLength(String commentText) {
        if (commentText != null && commentText.length() > MAX_COMMENT_LENGTH) {
            throw new IllegalArgumentException("El comentario no puede superar 255 caracteres");
        }
    }

    @Override
    public boolean containsOffensiveContent(String comment) {
        if (comment == null) {
            return false;
        }

        String normalizedComment = normalizeText(comment);

        for (String offensiveWord : OFFENSIVE_WORDS) {
            if (normalizedComment.contains(offensiveWord)) {
                return true;
            }
        }

        return false;
    }

    private String normalizeText(String text) {
        String lowerCaseText = text.toLowerCase(Locale.ROOT);
        String normalized = Normalizer.normalize(lowerCaseText, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "");
    }
}
