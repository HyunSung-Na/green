package com.green.demo.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.demo.controller.user.SignUpRequest;
import com.green.demo.error.NotFoundException;
import com.green.demo.mail.EmailService;
import com.green.demo.model.common.Name;
import com.green.demo.model.user.Email;
import com.green.demo.model.user.Role;
import com.green.demo.model.user.User;
import com.green.demo.security.Jwt;
import com.green.demo.service.user.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private Jwt jwt;
    @MockBean
    EmailService emailService;

    public static final Long ERROR_ID = 99L;

    public static Long TEST_ID;

    public static final String API_USER_URL = "/api/user/";

    private Name name;

    private String password;

    private String apiToken;

    @BeforeAll
    void setUp() {
        name = new Name("tester");
        String email = "test@gmail.com";
        password = "임의패스워드실험";

        User user = userService.join(name, new Email(email), password);
        apiToken = "Bearer "  + user.newApiToken(jwt, new String[]{Role.USER.value()});
        TEST_ID = user.getId();
    }

    @Test
    @Order(1)
    void 회원가입() throws Exception{
        String email = "oceana@email.com";
        SignUpRequest signUpRequest = signUpRequest(email);

        MvcResult mvcResult = mockMvc.perform(post("/api/join")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(signUpRequest)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        User user = userService.findByEmail(new Email(email))
                .orElseThrow(() -> new NotFoundException(User.class, email));

        assertNotNull(user);
        assertEquals(user.getName(), name);
        assertEquals(user.getEmail(), new Email(email));
        assertNotNull(user.getEmailCheckToken());
    }

    @Test
    @Order(2)
    void 회원조회실패() throws Exception{

        mockMvc.perform(get(API_USER_URL + "info/" + ERROR_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .header("api_key", apiToken))
                .andDo(print())
                .andExpect(status().is(401))
                .andReturn();
    }

    @Test
    @Order(3)
    void 회원조회() throws Exception{

        mockMvc.perform(get(API_USER_URL + "info/" + TEST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .header("api_key", apiToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @Order(4)
    void 회원목록조회() throws Exception{

        Pageable page = PageRequest.of(0, 10);

        mockMvc.perform(get(API_USER_URL + "info/list")
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", String.valueOf(page.getPageNumber()))
                .param("size", String.valueOf(page.getPageSize()))
                .header("api_key", apiToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @Order(5)
    void 회원목록조회실패() throws Exception{

        Pageable page = PageRequest.of(0, 10);

        mockMvc.perform(get(API_USER_URL + "info/list")
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", String.valueOf(page.getPageNumber()))
                .param("size", String.valueOf(page.getPageSize()))
                .header("api_key", "wrongApiKey"))
                .andDo(print())
                .andExpect(status().is(401))
                .andReturn();
    }

    @Test
    @Order(6)
    void 회원비밀번호수정실패_패스워드길이() throws Exception{
        mockMvc.perform(post(API_USER_URL + "settings/password")
                .contentType(MediaType.APPLICATION_JSON)
                .param("newPassword", "wrong")
                .header("api_key", apiToken))
                .andDo(print())
                .andExpect(status().is(400))
                .andReturn();
    }

    @Test
    @Order(7)
    void 회원비밀번호수정() throws Exception{
        mockMvc.perform(post(API_USER_URL + "settings/password")
                .contentType(MediaType.APPLICATION_JSON)
                .param("newPassword", "newPassword")
                .header("api_key", apiToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @Order(8)
    void 회원삭제실패() throws Exception{

        mockMvc.perform(delete(API_USER_URL + "remove/" + ERROR_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .header("api_key", apiToken))
                .andDo(print())
                .andExpect(status().is(401))
                .andReturn();
    }

    @Test
    @Order(9)
    void 회원삭제() throws Exception{

        mockMvc.perform(delete(API_USER_URL + "remove/" + TEST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .header("api_key", apiToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @AfterAll
    void After() {
        userService.deleteById(TEST_ID);
    }

    private SignUpRequest signUpRequest(String email) {
        return new SignUpRequest(name, email, password);
    }

}
