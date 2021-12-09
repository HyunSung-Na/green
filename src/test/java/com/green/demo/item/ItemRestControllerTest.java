package com.green.demo.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.demo.controller.item.ItemCreateDto;
import com.green.demo.controller.item.ItemDto;
import com.green.demo.controller.item.ItemUpdateDto;
import com.green.demo.model.common.Name;
import com.green.demo.model.user.Email;
import com.green.demo.model.user.Role;
import com.green.demo.model.user.User;
import com.green.demo.security.Jwt;
import com.green.demo.service.item.ItemService;
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
public class ItemRestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private Jwt jwt;
    private String apiToken;

    private ItemDto item;
    private User user;
    private static Long TEST_ID;
    private static final Long ERROR_ID = 99L;

    private static final String API_ITEM_URL = "/api/item/";

    @BeforeAll
    void setUp() {
        Name name = new Name("tester");
        String email = "test@gmail.com";
        String password = "임의패스워드실험";

        user = userService.join(name, new Email(email), password);
        apiToken = "Bearer "  + user.newApiToken(jwt, new String[]{"ROLE_USER"});
        item = itemService.createItem(createDto(), user.getEmail());
        TEST_ID = item.getId();
    }

    @Test
    @Order(1)
    void 상품등록() throws Exception{

        ItemCreateDto itemCreateDto = createDto();

        mockMvc.perform(post(API_ITEM_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("api_key", apiToken)
                .content(new ObjectMapper().writeValueAsString(itemCreateDto)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Order(2)
    void 상품수정() throws Exception{

        ItemUpdateDto updateDto = ItemUpdateDto.builder()
                .itemName("용월금")
                .description("새로운 다육이!")
                .sellingPrice(100000)
                .status("판매중")
                .unitSales(1)
                .build();

        mockMvc.perform(put(API_ITEM_URL + item.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("api_key", apiToken)
                .content(new ObjectMapper().writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Order(3)
    void 상품조회() throws Exception{
        mockMvc.perform(get(API_ITEM_URL + "info/" + TEST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .header("api_key", apiToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @Order(4)
    void 상품리스트조회() throws Exception{
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
    void 상품삭제() throws Exception{
        mockMvc.perform(delete(API_ITEM_URL + TEST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .header("api_key", apiToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @AfterAll
    void after() {
        itemService.deleteItem(user.getEmail(), item.getId());
        userService.deleteById(user.getId());
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
