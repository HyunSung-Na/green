package com.green.demo.review;

import com.green.demo.controller.item.ItemCreateDto;
import com.green.demo.controller.item.ItemDto;
import com.green.demo.controller.review.ReviewCreateDto;
import com.green.demo.controller.review.ReviewDto;
import com.green.demo.controller.review.ReviewUpdateDto;
import com.green.demo.model.common.Name;
import com.green.demo.model.review.Review;
import com.green.demo.model.user.Email;
import com.green.demo.model.user.User;
import com.green.demo.service.item.ItemService;
import com.green.demo.service.review.ReviewService;
import com.green.demo.service.user.UserService;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
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
public class ReviewServiceTest {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private User user;
    private ItemDto item;
    private ReviewDto review;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @BeforeAll
    void setUp() {
        Name name = new Name("tester");
        Email email = new Email("test@gmail.com");
        String password = "임의패스워드실험";
        user = userService.join(name, email, password);
        item = itemService.createItem(createItemDto(), user.getEmail());
    }

    @Test
    @Order(1)
    void 리뷰작성() throws Exception{
        review = reviewService.writeReview(createDto(), user.getEmail());

        assertThat(review, is(notNullValue()));
        assertThat(review.getSeq(), is(notNullValue()));
        log.info("Inserted review: {}", review);
    }

    @Test
    @Order(2)
    void 리뷰수정() throws Exception{
        ReviewUpdateDto updateDto = ReviewUpdateDto.builder()
                .title("reviewUpdate")
                .contents("newUpdate contents")
                .build();

        review = reviewService.updateReview(updateDto, user.getEmail(), review.getSeq());
        assertThat(review, is(notNullValue()));
        assertThat(review.getSeq(), is(notNullValue()));
        assertThat(review.getTitle(), is("reviewUpdate"));
        assertThat(review.getContents(), is("newUpdate contents"));
        log.info("Update review: {}", review);
    }

    @Test
    @Order(3)
    void 리뷰조회() throws Exception{
        ReviewDto newReview = reviewService.reviewDetail(review.getSeq());
        assertThat(newReview.getTitle(), is(review.getTitle()));
        assertThat(newReview.getContents(), is(review.getContents()));
        assertThat(newReview.getSeq(), is(review.getSeq()));
        log.info("view reviewDetail: {}", review);
    }

    @Test
    @Order(4)
    void 리뷰리스트조회() throws Exception{
        PageRequest page = PageRequest.of(0, 10);

        List<ReviewDto> reviews = reviewService.reviews(page);
        assertThat(reviews, is(notNullValue()));
        log.info("view reviews: {}", reviews);
    }

    @Test
    @Order(5)
    void 리뷰삭제() throws Exception{
        reviewService.deleteReview(review.getSeq(), user.getEmail());
        assertThat(reviewService.findByTitle(review.getTitle()), is(nullValue()));
        log.info("delete review: {}", review);
    }

    @AfterAll
    void after() {
        itemService.deleteItem(user.getEmail(), item.getSeq());
        userService.deleteById(user.getSeq());
    }

    private ReviewCreateDto createDto() {
        String title = "리뷰제목";
        String contents = "리뷰 내용";

        return ReviewCreateDto.builder()
                .title(title)
                .contents(contents)
                .itemId(item.getSeq())
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
