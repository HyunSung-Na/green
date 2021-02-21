package com.green.demo.review;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.green.demo.service.item.ItemService;
import com.green.demo.service.review.ReviewService;
import com.green.demo.service.user.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
public class ReviewRestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private Jwt jwt;
    private String apiToken;

    private ItemDto item;
    private ReviewDto review;
    private User user;
    private static Long TEST_ID;

    private static final String API_ITEM_URL = "/api/review/";

    @BeforeAll
    void setUp() {
        Name name = new Name("tester");
        String email = "test@gmail.com";
        String password = "임의패스워드실험";

        user = userService.join(name, new Email(email), password);
        apiToken = "Bearer "  + user.newApiToken(jwt, new String[]{Role.USER.value()});

        item = itemService.createItem(createDto(), user.getEmail());
        review = reviewService.writeReview(reviewCreateDto(), user.getEmail());
        TEST_ID = review.getId();
    }

    @Test
    @Order(1)
    void 리뷰등록() throws Exception {

        mockMvc.perform(post(API_ITEM_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("api_key", apiToken)
                .content(new ObjectMapper().writeValueAsString(reviewCreateDto())))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Order(2)
    void 리뷰수정() throws Exception {

        ReviewUpdateDto updateDto = ReviewUpdateDto.builder()
                .title("review update")
                .contents("contents update")
                .build();

        mockMvc.perform(put(API_ITEM_URL + review.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("api_key", apiToken)
                .content(new ObjectMapper().writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Order(3)
    void 리뷰조회() throws Exception {
        mockMvc.perform(get(API_ITEM_URL + "info/" + TEST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .header("api_key", apiToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @Order(4)
    void 리뷰리스트() throws Exception {
        Pageable page = PageRequest.of(0, 10);

        mockMvc.perform(get(API_ITEM_URL + "info/" + "list")
                .contentType(MediaType.APPLICATION_JSON)
                .header("api_key", apiToken)
                .param("page", String.valueOf(page.getPageNumber()))
                .param("size", String.valueOf(page.getPageSize())))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @Order(5)
    void 리뷰삭제() throws Exception {
        mockMvc.perform(delete(API_ITEM_URL + TEST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .header("api_key", apiToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @AfterAll
    void after() {
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

    private ItemCreateDto createDto() {
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
