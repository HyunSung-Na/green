package com.green.demo.repository.comment;

import com.green.demo.model.comment.Comment;
import com.green.demo.model.comment.QComment;
import com.green.demo.model.review.QReview;
import com.green.demo.model.user.QUser;
import com.green.demo.util.Querydsl4RepositorySupport;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.green.demo.model.comment.QComment.*;
import static com.green.demo.model.review.QReview.*;
import static com.green.demo.model.user.QUser.*;

@Repository
public class CommentRepositoryImpl extends Querydsl4RepositorySupport implements CommentRepositoryCustom{

    protected CommentRepositoryImpl() {
        super(Comment.class);
    }

    @Override
    public List<Comment> findReviewByIdWithComments(long reviewId) {
        return selectFrom(comment)
                .join(comment.review, review).fetchJoin()
                .join(comment.user, user).fetchJoin()
                .where(review.id.eq(reviewId))
                .fetch();
    }
}
