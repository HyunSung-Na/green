package com.green.demo.repository.post;

import com.green.demo.model.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    Post findByTitle(String title);
}
