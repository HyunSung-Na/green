package com.green.demo.repository.comment;

import com.green.demo.model.comment.Comment;
import com.green.demo.model.comment.QComment;
import com.green.demo.model.review.QReview;
import com.green.demo.model.user.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Comment> findReviewByIdWithComments(long reviewId) {
        return jpaQueryFactory.selectFrom(QComment.comment)
                .join(QComment.comment.review, QReview.review).fetchJoin()
                .leftJoin(QComment.comment.user, QUser.user).fetchJoin()
                .where(QReview.review.id.eq(reviewId))
                .fetch();
    }
}
