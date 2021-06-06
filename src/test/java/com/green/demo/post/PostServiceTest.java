package com.green.demo.post;

import com.green.demo.controller.post.PostCreateDto;
import com.green.demo.controller.post.PostDto;
import com.green.demo.model.common.Name;
import com.green.demo.model.user.Email;
import com.green.demo.model.user.User;
import com.green.demo.service.post.PostService;
import com.green.demo.service.user.UserService;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PostServiceTest {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private User user;
    private PostDto post;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        Name name = new Name("tester");
        Email email = new Email("test@gmail.com");
        String password = "임의패스워드실험";
        user = userService.join(name, email, password);
    }

    @Test
    @Order(1)
    void 게시판작성() throws Exception {
        post = postService.write(createDto(), user.getEmail());

        assertThat(post, is(notNullValue()));
        assertThat(post.getId(), is(notNullValue()));
        log.info("Write post: {}", post);
    }

    private PostCreateDto createDto() {
        String title = "title";
        String contents = "contents";

        return PostCreateDto.builder()
                .title(title)
                .contents(contents)
                .build();
    }
}
