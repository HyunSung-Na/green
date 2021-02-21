package com.green.demo.controller.comment;

import com.green.demo.model.comment.Comment;
import com.green.demo.model.common.Name;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentDto {

    private Long id;

    private String contents;

    private Name writer;

    private Integer level;

    private Boolean live;

    private Long reviewId;

    private Long superCommentId;

    private LocalDateTime createAt;

    private LocalDateTime modifyAt;

    @Builder
    public CommentDto(Long id, String contents, Name writer, Integer level, Boolean live, Long reviewId, Long superCommentId,
                      LocalDateTime createAt, LocalDateTime modifyAt) {
        this.id = id;
        this.contents = contents;
        this.writer = writer;
        this.level = level;
        this.live = live;
        this.reviewId = reviewId;
        this.superCommentId = superCommentId;
        this.createAt = createAt;
        this.modifyAt = modifyAt;
    }

    public static CommentDto of(Comment comment) {

        return CommentDto.builder()
                .id(comment.getId())
                .contents(comment.getContents())
                .level(comment.getLevel())
                .writer(comment.getWriter())
                .live(comment.getLive())
                .reviewId(comment.getReview().getId())
                .superCommentId((comment.getSuperComment() == null) ? null : comment.getSuperComment().getId())
                .build();
    }
}
