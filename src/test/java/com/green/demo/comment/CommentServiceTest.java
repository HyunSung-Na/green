package com.green.demo.comment;

import com.green.demo.controller.comment.CommentCreateDto;
import com.green.demo.controller.comment.CommentDto;
import com.green.demo.controller.comment.CommentUpdateDto;
import com.green.demo.controller.item.ItemCreateDto;
import com.green.demo.controller.item.ItemDto;
import com.green.demo.controller.review.ReviewCreateDto;
import com.green.demo.controller.review.ReviewDto;
import com.green.demo.model.comment.Comment;
import com.green.demo.model.common.Name;
import com.green.demo.model.review.Review;
import com.green.demo.model.user.Email;
import com.green.demo.model.user.User;
import com.green.demo.service.comment.CommentService;
import com.green.demo.service.item.ItemService;
import com.green.demo.service.review.ReviewService;
import com.green.demo.service.user.UserService;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommentServiceTest {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private User user;
    private ItemDto item;
    private ReviewDto review;
    private CommentDto comment;

    @Autowired
    private UserService userService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private CommentService commentService;

    @BeforeAll
    void setUp() {
        Name name = new Name("tester");
        Email email = new Email("test@gmail.com");
        String password = "임의패스워드실험";
        user = userService.join(name, email, password);
        item = itemService.createItem(createItemDto(), user.getEmail());
        review = reviewService.writeReview(createDto(), user.getEmail());
    }

    @Test
    @Order(1)
    void 댓글작성() {
        CommentCreateDto createDto = new CommentCreateDto("comment", review.getId(), null);

        comment = commentService.write(createDto, user.getId());

        assertThat(comment, is(notNullValue()));
        assertThat(comment.getId(), is(notNullValue()));
        log.info("Inserted comment: {}", comment);
    }

    @Test
    @Order(2)
    void 댓글수정() {
        CommentUpdateDto updateDto = new CommentUpdateDto("comment update", review.getId(), comment.getId());

        comment = commentService.update(updateDto, user.getId());
        assertThat(comment, is(notNullValue()));
        assertThat(comment.getId(), is(notNullValue()));
        assertThat(comment.getContents(), is("comment update"));
        log.info("Update comment: {}", comment);
    }

    @Test
    @Order(3)
    void 댓글조회() {
        List<CommentDto> comments = commentService.reviewDetail(review.getId());
        assertThat(comments, is(notNullValue()));
        log.info("comments view: {}", comments);
    }

    @Test
    @Order(4)
    void 댓글삭제() {
        commentService.delete(comment.getId(), user.getId());
        assertThat(commentService.findByContents(comment.getContents()), is(nullValue()));
        log.info("delete comment: {}", comment);
    }

    @AfterAll
    void after() {
        reviewService.deleteReview(review.getId(), user.getEmail());
        itemService.deleteItem(user.getEmail(), item.getId());
        userService.deleteById(user.getId());
    }

    private ReviewCreateDto createDto() {
        String title = "리뷰제목";
        String contents = "리뷰 내용";

        return ReviewCreateDto.builder()
                .title(title)
                .contents(contents)
                .itemId(item.getId())
                .build();
    }

    private ItemCreateDto createItemDto() {
        String itemName = "마르스";
        Name owner = user.getName();
        String description = "희귀한 품종 마르스!";
        int sellingPrice = 800000;
        int unitSales = 1;
        String status = "판매중";

        return ItemCreateDto.builder()
                .itemName(itemName)
                .description(description)
                .owner(owner)
                .sellingPrice(sellingPrice)
                .status(status)
                .unitSales(unitSales)
                .build();
    }
}
