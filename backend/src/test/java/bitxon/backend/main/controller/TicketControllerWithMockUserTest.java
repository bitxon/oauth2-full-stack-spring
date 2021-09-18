package bitxon.backend.main.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import bitxon.backend.main.model.Ticket;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TicketControllerWithMockUserTest {

    private static final String TEST_USER_ID = "86783e07-b0d2-4470-b287-c899bc2aa09c";

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(SecurityMockMvcConfigurers.springSecurity())
            .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @WithMockUser(username = "not-required", roles = "app-admin")
    void searchAsAdmin() throws Exception {
        List<Ticket> expectedTickets = List.of(
            Ticket.builder().id("1").passengerId(TEST_USER_ID).arrival("JFK").departure("LAS").build(),
            Ticket.builder().id("3").passengerId("ANY_OTHER").arrival("LAS").departure("ATL").build(),
            Ticket.builder().id("2").passengerId(TEST_USER_ID).arrival("LAS").departure("JFK").build(),
            Ticket.builder().id("4").passengerId("ANY_OTHER").arrival("ATL").departure("LAS").build()
        );
        String expectedJson = objectMapper.writeValueAsString(expectedTickets);

        mvc.perform(
                get("/tickets")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json(expectedJson));
    }

    @Test
    @WithMockUser(username = TEST_USER_ID, roles = "app-user")
    void searchAsUser() throws Exception {
        List<Ticket> expectedTickets = List.of(
            Ticket.builder().id("1").passengerId(TEST_USER_ID).arrival("JFK").departure("LAS").build(),
            Ticket.builder().id("2").passengerId(TEST_USER_ID).arrival("LAS").departure("JFK").build()
        );
        String expectedJson = objectMapper.writeValueAsString(expectedTickets);

        mvc.perform(
                get("/tickets")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json(expectedJson));
    }

    @Test
    @WithAnonymousUser
    void searchAsAnonymous() throws Exception {
        mvc.perform(
                get("/tickets")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isUnauthorized());
    }
}