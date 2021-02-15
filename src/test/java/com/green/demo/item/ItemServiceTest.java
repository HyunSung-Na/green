package com.green.demo.item;

import com.green.demo.controller.item.ItemCreateDto;
import com.green.demo.controller.item.ItemDto;
import com.green.demo.controller.item.ItemUpdateDto;
import com.green.demo.model.common.Name;
import com.green.demo.model.user.Email;
import com.green.demo.model.user.User;
import com.green.demo.service.item.ItemService;
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
public class ItemServiceTest {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private User user;
    private ItemDto item;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    @BeforeAll
    void setUp() {
        Name name = new Name("tester");
        Email email = new Email("test@gmail.com");
        String password = "임의패스워드실험";

        user = userService.join(name, email, password);
    }

    @Test
    @Order(1)
    void 상품추가() throws Exception{
        item = itemService.createItem(createDto(), user.getEmail());

        assertThat(item, is(notNullValue()));
        assertThat(item.getSeq(), is(notNullValue()));
        log.info("Inserted item: {}", item);
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

        item = itemService.updateItem(user.getEmail(), item.getSeq(), updateDto);
        assertThat(item, is(notNullValue()));
        assertThat(item.getItemName(), is("용월금"));
        assertThat(item.getDescription(), is("새로운 다육이!"));
        assertThat(item.getSellingPrice(), is(100000));
        assertThat(item.getStatus(), is("판매중"));
        assertThat(item.getUnitSales(), is(1));
        log.info("Update item: {}", item);
    }

    @Test
    @Order(3)
    void 상품조회() throws Exception{
        ItemDto itemDetail = itemService.detailItem(item.getSeq());
        assertThat(itemDetail, is(notNullValue()));
        log.info("view item: {}", itemDetail);
    }

    @Test
    @Order(4)
    void 상품리스트조회() throws Exception{
        PageRequest page = PageRequest.of(0, 10);

        List<ItemDto> itemDtoList = itemService.items(page);
        assertThat(itemDtoList, is(notNullValue()));
        log.info("view items: {}", itemDtoList);
    }

    @Test
    @Order(5)
    void 상품삭제() throws Exception{
        itemService.deleteItem(user.getEmail(), item.getSeq());
        assertThat(itemService.findByName(item.getItemName()), is(nullValue()));
        log.info("delete item: {}", item);
    }

    @AfterAll
    void after() {
        userService.deleteById(user.getSeq());
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
