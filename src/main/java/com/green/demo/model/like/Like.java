package com.green.demo.model.like;

import com.green.demo.model.review.Review;
import com.green.demo.model.user.User;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "likes")
public class Like {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Review review;
}
