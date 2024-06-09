package com.example.psyBack.service;

import com.example.psyBack.entity.Review;
import com.example.psyBack.repository.ReviewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);
    private ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(
            ReviewRepository reviewRepository
    )
    {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> getBadNoAcceptedReviews()
    {
        return reviewRepository.findAll()
                .stream()
                .filter(e -> !e.isStatus() && !e.isLiked())
                .toList();
    }

    public List<Review> getGoodNoAcceptedReviews()
    {
        return reviewRepository.findAll()
                .stream()
                .filter(e -> !e.isStatus() && e.isLiked())
                .toList();
    }

    public Review changeStatus(int reviewId)
    {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Такого отзыва нет"));

        if (review.isStatus())
        {
            logger.error("Отзыв с id = " + reviewRepository + " уже подтвержден!");
            throw new RuntimeException("Этот отзыв подтвержден!");
        }

        review.setStatus(true);
        review.setLiked(true);
        Review savedReview = reviewRepository.save(review);
        logger.info("Статус у отзыва с id = " + reviewId + " успешно обнавлен");

        return savedReview;
    }
}
