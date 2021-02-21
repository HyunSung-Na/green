package com.green.demo.controller.review;

import com.green.demo.model.common.Name;
import com.green.demo.model.common.Star;
import com.green.demo.model.review.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ReviewDto {

    private Long id;

    private String title;

    private String contents;

    private Name writer;

    private String reviewImageUrl;

    private int commentCount;

    private Star star;

    private LocalDateTime createAt;

    @Builder
    private ReviewDto(Long id, String title, String contents, Name writer,
                     String reviewImageUrl, int commentCount, Star star, LocalDateTime createAt) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.writer = writer;
        this.reviewImageUrl = reviewImageUrl;
        this.commentCount = commentCount;
        this.star = star;
        this.createAt = createAt;
    }

    public static ReviewDto of(Review review) {

        return ReviewDto.builder()
                .id(review.getId())
                .title(review.getTitle())
                .contents(review.getContents())
                .writer(review.getWriter())
                .reviewImageUrl(review.getReviewImageUrl())
                .commentCount(review.getCommentCount())
                .star(review.getStar())
                .createAt(review.getCreateAt())
                .build();
    }
}
