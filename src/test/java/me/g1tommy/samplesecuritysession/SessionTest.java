package me.g1tommy.samplesecuritysession;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.g1tommy.samplesecuritysession.domain.dto.LoginRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class SessionTest {

    private MockMvc mvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(@Autowired WebApplicationContext applicationContext) {
        mvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .alwaysDo(print())
                .build();

        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Login Test - 200")
    void givenUser_whenLogin_thenOK() throws Exception {
        mvc.perform(post("/auth/login")
                        .content(objectMapper.writeValueAsString(new LoginRequestDto("admin", "admin")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Login Test - 401")
    void givenWrongUser_whenLogin_then401() throws Exception {
        mvc.perform(post("/auth/login")
                        .content(objectMapper.writeValueAsString(new LoginRequestDto("admin", "wrong")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Validated Session Test - OK")
    void givenSession_whenCheck_thenOK() throws Exception {
        var session = mvc.perform(post("/auth/login")
                        .content(objectMapper.writeValueAsString(new LoginRequestDto("admin", "admin")))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andReturn().getRequest().getSession();

        mvc.perform(get("/auth/validate")
                        .session((MockHttpSession) session)
                        .content(objectMapper.writeValueAsString(new LoginRequestDto("admin", "wrong"))))
                .andExpect(request().sessionAttribute("principalName", "admin"));
    }

    @Test
    @DisplayName("Logout Test - 200")
    void givenUser_whenLogout_then200() throws Exception {
        givenUser_whenLogin_thenOK();

        mvc.perform(delete("/auth/logout"))
                .andExpect(request().sessionAttributeDoesNotExist("principalName"))
                .andExpect(status().isOk());
    }

}
