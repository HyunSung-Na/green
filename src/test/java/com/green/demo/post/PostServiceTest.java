package com.green.demo.post;

import com.green.demo.model.user.User;
import com.green.demo.service.post.PostService;
import com.green.demo.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PostServiceTest {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private User user;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {

    }
}
