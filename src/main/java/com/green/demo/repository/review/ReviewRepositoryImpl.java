package com.green.demo.repository.review;

import com.green.demo.model.item.QItem;
import com.green.demo.model.review.Review;
import com.green.demo.model.user.QUser;
import com.green.demo.util.Querydsl4RepositorySupport;

import org.springframework.stereotype.Repository;

import static com.green.demo.model.review.QReview.*;

@Repository
public class ReviewRepositoryImpl extends Querydsl4RepositorySupport implements ReviewRepositoryCustom {

    protected ReviewRepositoryImpl() {
        super(Review.class);
    }

    @Override
    public Review findReviewByIdWithComments(long reviewId) {
        return selectFrom(review)
                .leftJoin(review.user, QUser.user).fetchJoin()
                .leftJoin(review.item, QItem.item).fetchJoin()
                .where(review.id.eq(reviewId))
                .fetch()
                .get(0);
    }
}
