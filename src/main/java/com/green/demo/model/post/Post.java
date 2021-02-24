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
    public Post(Long id, String title, String contents, Name writer, String thumbnail,
                int commentCount, LocalDateTime createAt, LocalDateTime modifyAt, User user) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.writer = writer;
        this.thumbnail = thumbnail;
        this.commentCount = commentCount;
        this.createAt = createAt;
        this.modifyAt = modifyAt;
        this.user = user;
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
