package com.green.demo.service.comment;


import com.green.demo.controller.comment.CommentCreateDto;
import com.green.demo.controller.comment.CommentDto;
import com.green.demo.controller.comment.CommentUpdateDto;
import com.green.demo.error.NotFoundException;
import com.green.demo.error.UnauthorizedException;
import com.green.demo.model.comment.Comment;
import com.green.demo.model.review.Review;
import com.green.demo.model.user.User;
import com.green.demo.repository.comment.CommentRepository;
import com.green.demo.service.review.ReviewService;
import com.green.demo.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ReviewService reviewService;

    public CommentDto write(CommentCreateDto createDto, Long userId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class, userId));
        Review review = reviewService.findById(createDto.getReviewId())
                .orElseThrow(() -> new NotFoundException(Review.class, createDto.getReviewId()));

        if (createDto.getSuperCommentId() != null) {
            Comment newComment =  subCommentOf(createDto, user, review);
            accountAndReviewAddComment(newComment, user, review);
            return CommentDto.of(newComment);
        }

        Comment newComment = of(createDto, user, review);
        accountAndReviewAddComment(newComment, user, review);

        return CommentDto.of(newComment);
    }

    private Comment of(CommentCreateDto createDto, User user, Review review) {
        Comment newComment  = Comment.builder()
                .contents(createDto.getContents())
                .writer(user.getName())
                .live(true)
                .level(1)
                .review(review)
                .user(user)
                .build();

        return commentRepository.save(newComment);
    }

    private Comment subCommentOf(CommentCreateDto createDto, User user, Review review) {
        Comment supComment = commentRepository.findById(createDto.getSuperCommentId())
                .orElseThrow(() -> new NotFoundException(Comment.class, createDto.getSuperCommentId()));

        if (!supComment.getLive()) {
            throw new RuntimeException("comment is not exist");
        }

        Comment newComment = Comment.builder()
                .contents(createDto.getContents())
                .writer(user.getName())
                .live(true)
                .level(supComment.getLevel() + 1)
                .review(review)
                .superComment(supComment)
                .user(user)
                .build();

        supComment.getSubComment().add(newComment);
        commentRepository.save(supComment);

        return commentRepository.save(newComment);
    }

    private void accountAndReviewAddComment(Comment newComment, User user, Review review) {
        user.getComments().add(newComment);
        userService.insert(user);

        review.getComments().add(newComment);
        review.commentCountPlus();
        reviewService.insert(review);
    }

    @Transactional(readOnly = true)
    public List<CommentDto> reviewDetail(Long reviewId) {
        List<Comment> comments = commentRepository.findReviewByIdWithComments(reviewId);
        if (comments.size() == 0) {
            return Collections.emptyList();
        }

        return comments.stream()
                .map(CommentDto::of)
                .collect(Collectors.toList());
    }

    public CommentDto update(CommentUpdateDto updateDto, Long userId) {
        Comment comment = commentRepository.findById(updateDto.getCommentId())
                .orElseThrow(() -> new NotFoundException(Comment.class, updateDto.getCommentId()));

        if (!comment.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("AuthenticationFailed");
        }

        comment.apply(updateDto.getContents());
        return CommentDto.of(comment);
    }

    public void delete(Long commentId, Long userId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(Comment.class, commentId));

        if (!comment.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("AuthenticationFailed");
        }

        commentRepository.delete(comment);
    }
}
