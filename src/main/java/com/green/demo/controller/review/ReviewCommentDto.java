package com.green.demo.controller.review;

import com.green.demo.controller.comment.CommentDto;
import com.green.demo.model.common.Name;
import com.green.demo.model.common.Star;
import com.green.demo.model.review.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class ReviewCommentDto {

    private Long id;

    private String title;

    private String contents;

    private Name writer;

    private String reviewImageUrl;

    private int commentCount;

    private Star star;

    private LocalDateTime createAt;

    private List<CommentDto> comments = new ArrayList<>();

    @Builder
    private ReviewCommentDto(Long id, String title, String contents, Name writer, String reviewImageUrl,
                            int commentCount, Star star, LocalDateTime createAt, List<CommentDto> comments) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.writer = writer;
        this.reviewImageUrl = reviewImageUrl;
        this.commentCount = commentCount;
        this.star = star;
        this.createAt = createAt;
        this.comments = comments;
    }

    public static ReviewCommentDto of(Review review) {
        List<CommentDto> comments = review.getComments().stream()
                .map(CommentDto::of)
                .collect(Collectors.toList());

        return ReviewCommentDto.builder()
                .id(review.getId())
                .title(review.getTitle())
                .contents(review.getContents())
                .reviewImageUrl(review.getReviewImageUrl())
                .writer(review.getWriter())
                .commentCount(review.getCommentCount())
                .star(review.getStar())
                .createAt(review.getCreateAt())
                .comments(comments)
                .build();
    }
}
