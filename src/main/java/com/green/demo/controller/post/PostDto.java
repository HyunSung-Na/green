package com.green.demo.controller.post;

import com.green.demo.model.common.Name;
import com.green.demo.model.post.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostDto {

    private Long id;

    private String title;

    private String contents;

    private Name writer;

    private String thumbnail;

    private int commentCount;

    private LocalDateTime createAt;

    private LocalDateTime modifyAt;

    @Builder
    private PostDto(Long id, String title, String contents, Name writer, String thumbnail,
                    int commentCount, LocalDateTime createAt, LocalDateTime modifyAt) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.writer = writer;
        this.thumbnail = thumbnail;
        this.commentCount = commentCount;
        this.createAt = createAt;
        this.modifyAt = modifyAt;
    }

    public static PostDto of(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .contents(post.getContents())
                .thumbnail(post.getThumbnail())
                .commentCount(post.getCommentCount())
                .writer(post.getWriter())
                .createAt(post.getCreateAt())
                .modifyAt(post.getModifyAt())
                .build();
    }
}

