package org.redsaberes.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.redsaberes.repository.ReviewRepository;
import org.redsaberes.service.impl.ReviewServiceImpl;
import org.redsaberes.service.validator.ReviewValidator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReviewServiceTest {
    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        ReviewRepository repo = Mockito.mock(ReviewRepository.class);
        ReviewValidator validator = Mockito.mock(ReviewValidator.class);
        reviewService = new ReviewServiceImpl(repo, validator);
    }

    @Test
    void testCommentLengthExceeded() {
        String longComment = "a".repeat(256);

        assertThrows(IllegalArgumentException.class, () -> reviewService.validateCommentLength(longComment));
    }

    @ParameterizedTest
    @ValueSource(strings = {"insulto1", "spam_link", "Puto cabron", "comentario_valido"})
    void testContentFilter(String comment) {

        if ("comentario_valido".equals(comment)) {
            assertFalse(reviewService.containsOffensiveContent(comment));
        } else {
            assertTrue(reviewService.containsOffensiveContent(comment));
        }
    }
}

