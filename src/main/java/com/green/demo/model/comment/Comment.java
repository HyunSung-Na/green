package com.green.demo.model.comment;

import com.green.demo.model.common.Name;
import com.green.demo.model.review.Review;
import com.green.demo.model.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDateTime.now;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String contents;

    @Column
    private Name writer;

    @Column
    private Integer level;

    private Boolean live;

    private LocalDateTime createAt;

    private LocalDateTime modifyAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch=FetchType.LAZY)
    private Comment superComment;

    @OneToMany(mappedBy = "superComment", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Comment> subComment = new ArrayList<>();

    @Builder
    public Comment(String contents, Name writer, Integer level,
                   Boolean live, Review review, User user, Comment superComment) {
        this(null, contents, writer, level, live, review, user, superComment);
    }

    @Builder
    public Comment(Long id, String contents, Name writer, Integer level,
                   Boolean live, Review review, User user, Comment superComment) {
        this.id = id;
        this.contents = contents;
        this.writer = writer;
        this.level = level;
        this.live = live;
        this.createAt = defaultIfNull(createAt, now());
        this.modifyAt = defaultIfNull(createAt, now());
        this.review = review;
        this.user = user;
        this.superComment = superComment;
    }

    public void apply(String contents) {
        this.contents = contents;
        this.modifyAt = defaultIfNull(createAt, now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Comment comment = (Comment) o;

        return new EqualsBuilder().append(id, comment.id).append(contents, comment.contents).append(writer, comment.writer).append(level, comment.level).append(live, comment.live).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(contents).append(writer).append(level).append(live).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("contents", contents)
                .append("writer", writer)
                .append("level", level)
                .append("live", live)
                .toString();
    }
}
