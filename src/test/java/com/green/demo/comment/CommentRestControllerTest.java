package com.green.demo.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.demo.controller.comment.CommentCreateDto;
import com.green.demo.controller.comment.CommentDto;
import com.green.demo.controller.comment.CommentUpdateDto;
import com.green.demo.controller.item.ItemCreateDto;
import com.green.demo.controller.item.ItemDto;
import com.green.demo.controller.review.ReviewCreateDto;
import com.green.demo.controller.review.ReviewDto;
import com.green.demo.controller.review.ReviewUpdateDto;
import com.green.demo.model.common.Name;
import com.green.demo.model.user.Email;
import com.green.demo.model.user.Role;
import com.green.demo.model.user.User;
import com.green.demo.security.Jwt;
import com.green.demo.service.comment.CommentService;
import com.green.demo.service.item.ItemService;
import com.green.demo.service.review.ReviewService;
import com.green.demo.service.user.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommentRestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private Jwt jwt;
    private String apiToken;

    private CommentDto comment;
    private ItemDto item;
    private ReviewDto review;
    private User user;
    private static Long TEST_ID;

    private static final String API_COMMENT_URL = "/api/comment/";


    @BeforeAll
    void setUp() {
        Name name = new Name("tester");
        String email = "test@gmail.com";
        String password = "임의패스워드실험";

        user = userService.join(name, new Email(email), password);
        apiToken = "Bearer "  + user.newApiToken(jwt, new String[]{Role.USER.value()});

        item = itemService.createItem(itemCreateDto(), user.getEmail());
        review = reviewService.writeReview(reviewCreateDto(), user.getEmail());

        CommentCreateDto createDto = new CommentCreateDto("comment", review.getId(), null);
        comment = commentService.write(createDto, user.getId());
        TEST_ID = comment.getId();
    }

    @Test
    @Order(1)
    void 댓글생성() throws Exception{

        CommentCreateDto createDto = new CommentCreateDto("newComment", review.getId(), null);

        mockMvc.perform(post(API_COMMENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("api_key", apiToken)
                .content(new ObjectMapper().writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Order(2)
    void 댓글수정() throws Exception {

        CommentUpdateDto updateDto = new CommentUpdateDto("comment update", review.getId(), comment.getId());

        mockMvc.perform(put(API_COMMENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("api_key", apiToken)
                .content(new ObjectMapper().writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Order(3)
    void 댓글조회() throws Exception {
        CommentCreateDto createDto = new CommentCreateDto("secondComment", review.getId(), null);
        commentService.write(createDto, user.getId());

        mockMvc.perform(get(API_COMMENT_URL + "info/" + review.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("api_key", apiToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @Order(4)
    void 댓글삭제() throws Exception {
        mockMvc.perform(delete(API_COMMENT_URL + TEST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .header("api_key", apiToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @AfterAll
    void after() {
        commentService.delete(comment.getId(), user.getId());
        reviewService.deleteReview(review.getId(), user.getEmail());
        itemService.deleteItem(user.getEmail(), item.getId());
        userService.deleteById(user.getId());
    }

    private ReviewCreateDto reviewCreateDto() {
        String title = "reviewTitle";
        String contents = "reviewContents";

        return ReviewCreateDto.builder()
                .title(title)
                .contents(contents)
                .itemId(item.getId())
                .build();
    }

    private ItemCreateDto itemCreateDto() {
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
