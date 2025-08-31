package com.fumology.carrental.controller;
import com.fumology.carrental.DTO.UserDTO;
import com.fumology.carrental.configuration.Security.JWTService;
import com.fumology.carrental.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@WebMvcTest(SecurityController.class)
class SecurityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JWTService jwtService;

    @MockitoBean
    private UserService userService;

    @Test
    void login_ShouldReturnToken_WhenAuthenticated() throws Exception {
        when(jwtService.generateToken(any())).thenReturn("fake-jwt");

        mockMvc.perform(get("/api/login")
                        .with(user("test@example.com").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }

    @Test
    void login_ShouldReturnUnauthorized_WhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/login"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAllUsers_ShouldReturnUsers_WhenAdmin() throws Exception {
        UserDTO user = new UserDTO("John", "Doe", "john@example.com", false, List.of());
        when(userService.getAllUsers()).thenReturn(List.of(user));

        mockMvc.perform(get("/api/users/all")
                        .with(user("admin@example.com").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].email").value("john@example.com"));
    }

}