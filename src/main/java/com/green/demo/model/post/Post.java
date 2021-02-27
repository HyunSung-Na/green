package com.green.demo.model.post;

import com.green.demo.model.comment.Comment;
import com.green.demo.model.common.Name;
import com.green.demo.model.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.google.common.base.Preconditions.checkArgument;
import static java.time.LocalDateTime.now;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Entity
@Getter
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String contents;

    @Column
    private Name writer;

    @Column
    private String thumbnail;

    @Column
    @Setter
    private int commentCount;

    @Column
    private LocalDateTime createAt;

    @Column
    private LocalDateTime modifyAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder
    public Post(String title, String contents, Name writer, User user) {
        this(null, title, contents, writer, null, 0, null, null, user);
    }

    @Builder
    public Post(Long id, String title, String contents, Name writer, String thumbnail,
                int commentCount, LocalDateTime createAt, LocalDateTime modifyAt, User user) {

        checkArgument(isNotEmpty(title), "title must be provided.");
        checkArgument(
                title.length() >= 2 && title.length() <= 50,
                "post title length must be between 2 and 50 characters."
        );

        checkArgument(isNotEmpty(contents), "contents must be provided.");
        checkArgument(
                contents.length() >= 4 && contents.length() <= 500,
                "post contents length must be between 4 and 500 characters."
        );

        this.id = id;
        this.title = title;
        this.contents = contents;
        this.writer = writer;
        this.thumbnail = thumbnail;
        this.commentCount = commentCount;
        this.createAt = defaultIfNull(createAt, now());;
        this.modifyAt = defaultIfNull(modifyAt, now());;
        this.user = user;
    }

    public void apply(String title, String contents) {

        checkArgument(isNotEmpty(title), "title must be provided.");
        checkArgument(
                title.length() >= 2 && title.length() <= 50,
                "post title length must be between 2 and 50 characters."
        );

        checkArgument(isNotEmpty(contents), "contents must be provided.");
        checkArgument(
                contents.length() >= 4 && contents.length() <= 500,
                "post contents length must be between 4 and 500 characters."
        );

        this.title = title;
        this.contents = contents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Post post = (Post) o;

        return new EqualsBuilder().append(commentCount, post.commentCount).append(id, post.id).append(title, post.title).append(contents, post.contents).append(writer, post.writer).append(thumbnail, post.thumbnail).append(createAt, post.createAt).append(modifyAt, post.modifyAt).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(title).append(contents).append(writer).append(thumbnail).append(commentCount).append(createAt).append(modifyAt).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("title", title)
                .append("contents", contents)
                .append("writer", writer)
                .append("thumbnail", thumbnail)
                .append("commentCount", commentCount)
                .append("createAt", createAt)
                .append("modifyAt", modifyAt)
                .toString();
    }
}
