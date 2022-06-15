package com.example.dits.controllers;

import com.example.dits.DAO.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
public class AdminHomeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserRepository repository;

    @Test
    public void testUsersListOnAdmin() throws Exception {
        mockMvc.perform(get("/admin/usersList"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetUsersOnAdmin() throws Exception {
        mockMvc.perform(get("/admin/getUsers"))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddUserOnAdmin() throws Exception {
        mockMvc.perform(post("/admin/addUser")
                .param("userId","1")
                .param("firstName","yu")
                .param("surname","yu")
                .param("role","ROLE_ADMIN")
                .param("login","yu")
                .param("password","yu").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    public void testEditUserOnAdmin() throws Exception {
        mockMvc.perform(put("/admin/editUser")
                .param("userId","1")
                .param("firstName","yu")
                .param("surname","yu")
                .param("role","ROLE_ADMIN")
                .param("login","yu")
                .param("password","yu").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    public void testRemoveUserOnAdmin() throws Exception {
        mockMvc.perform(delete("/admin/removeUser").param("userId","1").with(csrf()))
                .andExpect(status().isOk());
    }
}