package org.redsaberes.service.impl;

import org.redsaberes.repository.ReviewRepository;
import org.redsaberes.repository.impl.ReviewRepositoryImpl;
import org.redsaberes.service.ReviewService;

public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl() {
        this(new ReviewRepositoryImpl());
    }

    ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }
}
