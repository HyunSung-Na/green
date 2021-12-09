package com.green.demo.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.demo.mail.EmailService;
import com.green.demo.model.common.Name;
import com.green.demo.model.user.Email;
import com.green.demo.model.user.Role;
import com.green.demo.model.user.User;
import com.green.demo.security.AuthenticationRequest;
import com.green.demo.security.Jwt;
import com.green.demo.service.user.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthenticateControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private Jwt jwt;
    @MockBean
    EmailService emailService;

    private User user;

    private Long TEST_ID;

    private Name name;

    private String credentials;

    private String principal;

    private String apiToken;

    @BeforeAll
    void setUp() {
        name = new Name("tester");
        principal = "test@gmail.com";
        credentials = "임의패스워드실험";

        user = userService.join(name, new Email(principal), credentials);
        apiToken = "Bearer "  + user.newApiToken(jwt, new String[]{"ROLE_USER"});
        TEST_ID = user.getId();
    }

    @Test
    @Order(1)
    void 로그인() throws Exception{
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(principal, credentials);

        MvcResult mvcResult = mockMvc.perform(post("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(authenticationRequest)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    @Order(2)
    void 로그인실패() throws Exception{
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(principal, "이상한비밀번호입니다");

        MvcResult mvcResult = mockMvc.perform(post("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(authenticationRequest)))
                .andExpect(status().is(401))
                .andDo(print())
                .andReturn();
    }

    @Test
    @Order(3)
    void 이메일인증() throws Exception{
        MvcResult mvcResult = mockMvc.perform(get("/api/check-email-token")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("api_key", apiToken)
                .param("token", user.getEmailCheckToken())
                .param("email", principal))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    @Order(4)
    void 이메일인증실패() throws Exception{
        MvcResult mvcResult = mockMvc.perform(get("/api/check-email-token")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("api_key", apiToken)
                .param("token", "wrongToken")
                .param("email", principal))
                .andExpect(status().is(401))
                .andDo(print())
                .andReturn();
    }

    @AfterAll
    void After() {
        userService.deleteById(TEST_ID);
    }

}
