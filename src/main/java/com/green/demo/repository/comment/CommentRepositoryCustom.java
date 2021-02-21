package com.green.demo.repository.comment;

import com.green.demo.model.comment.Comment;

import java.util.List;

public interface CommentRepositoryCustom {
    List<Comment> findReviewByIdWithComments(long reviewId);
}
