package com.green.demo.repository.comment;

import com.green.demo.model.comment.Comment;
import com.green.demo.model.comment.QComment;
import com.green.demo.model.review.QReview;
import com.green.demo.model.user.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.green.demo.model.comment.QComment.*;
import static com.green.demo.model.review.QReview.*;
import static com.green.demo.model.user.QUser.*;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Comment> findReviewByIdWithComments(long reviewId) {
        return jpaQueryFactory.selectFrom(comment)
                .join(comment.review, review).fetchJoin()
                .join(comment.user, user).fetchJoin()
                .where(review.id.eq(reviewId))
                .fetch();
    }
}
