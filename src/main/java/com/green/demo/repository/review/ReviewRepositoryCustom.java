package com.green.demo.repository.review;

import com.green.demo.model.review.Review;

public interface ReviewRepositoryCustom {
    Review findReviewByIdWithComments(long reviewId);
}
