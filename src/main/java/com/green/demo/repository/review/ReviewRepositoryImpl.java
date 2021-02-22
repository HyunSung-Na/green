package com.green.demo.repository.review;

import com.green.demo.model.comment.QComment;
import com.green.demo.model.item.QItem;
import com.green.demo.model.review.QReview;
import com.green.demo.model.review.Review;
import com.green.demo.model.user.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Review findReviewByIdWithComments(long reviewId) {
        return jpaQueryFactory.selectFrom(QReview.review)
                .leftJoin(QReview.review.user, QUser.user).fetchJoin()
                .leftJoin(QReview.review.item, QItem.item).fetchJoin()
                .where(QReview.review.id.eq(reviewId))
                .fetch()
                .get(0);
    }
}
