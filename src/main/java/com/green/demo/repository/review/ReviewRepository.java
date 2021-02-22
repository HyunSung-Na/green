package com.green.demo.repository.review;

import com.green.demo.model.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {
    Review findByTitle(String title);
}
