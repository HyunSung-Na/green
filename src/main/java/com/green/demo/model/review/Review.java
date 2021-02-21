package com.green.demo.model.review;

import com.green.demo.model.comment.Comment;
import com.green.demo.model.common.Name;
import com.green.demo.model.item.Item;
import com.green.demo.model.common.Star;
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
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDateTime.now;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

@Entity
@Getter
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String contents;

    @Column
    @Enumerated
    private Name writer;

    @Column
    private String reviewImageUrl;

    @Column
    @Setter
    private int commentCount;

    @Column
    @Enumerated
    private Star star;

    @Column
    private LocalDateTime createAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private final List<Comment> comments = new ArrayList<>();

    @Builder
    public Review(String title, String contents, Name writer) {
        this(null, title, contents, writer, null, 0, new Star(0), null, null, null);
    }

    @Builder
    public Review(String title, String contents, Name writer, String reviewImageUrl, int commentCount,
                  Star star) {
        this(null, title, contents, writer, reviewImageUrl, commentCount, star, null, null, null);
    }

    @Builder
    public Review(Long id, String title, String contents, Name writer, String reviewImageUrl,
                  int commentCount, Star star, LocalDateTime createAt, User user, Item item) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.writer = writer;
        this.reviewImageUrl = reviewImageUrl;
        this.commentCount = commentCount;
        this.star = star;
        this.createAt = defaultIfNull(createAt, now());
        this.user = user;
        this.item = item;
    }

    public void apply(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

    public void setUserAndItem(User user, Item item) {
        this.user = user;
        this.item = item;
    }

    public void commentCountPlus() {
        this.commentCount++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Review review = (Review) o;

        return new EqualsBuilder().append(commentCount, review.commentCount).append(id, review.id).append(title, review.title).append(contents, review.contents).append(writer, review.writer).append(reviewImageUrl, review.reviewImageUrl).append(star, review.star).append(createAt, review.createAt).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(title).append(contents).append(writer).append(reviewImageUrl).append(commentCount).append(star).append(createAt).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("title", title)
                .append("contents", contents)
                .append("writer", writer)
                .append("reviewImageUrl", reviewImageUrl)
                .append("commentCount", commentCount)
                .append("star", star)
                .append("createAt", createAt)
                .toString();
    }
}
