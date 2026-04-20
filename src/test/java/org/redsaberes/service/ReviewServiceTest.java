package org.redsaberes.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.redsaberes.service.impl.ReviewServiceImpl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReviewServiceTest {

    @Test
    void testCommentLengthExceeded() {
        ReviewService reviewService = new ReviewServiceImpl();
        String longComment = "a".repeat(256);

        assertThrows(IllegalArgumentException.class, () -> reviewService.validateCommentLength(longComment));
    }

    @ParameterizedTest
    @ValueSource(strings = {"insulto1", "spam_link", "Puto cabron", "comentario_valido"})
    void testContentFilter(String comment) {
        ReviewService reviewService = new ReviewServiceImpl();

        if ("comentario_valido".equals(comment)) {
            assertFalse(reviewService.containsOffensiveContent(comment));
        } else {
            assertTrue(reviewService.containsOffensiveContent(comment));
        }
    }
}

