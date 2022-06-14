package com.example.dits.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
public class AdminStatisticControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testTestStatisticOnAdmin() throws Exception {
        mockMvc.perform(get("/admin/adminStatistic"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUserStatisticOnAdmin() throws Exception {
        mockMvc.perform(get("/admin/getUserStatistic"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUserTestsStatisticOnAdmin() throws Exception {
        mockMvc.perform(get("/admin/getUserTestsStatistic").param("id","1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testRemoveStatisticByUserIdOnAdmin() throws Exception {
        mockMvc.perform(get("/admin/adminStatistic/removeStatistic/byId").param("id","1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testRemoveAllStatisticOnAdmin() throws Exception {
        mockMvc.perform(get("/admin/adminStatistic/removeStatistic/all"))
                .andExpect(status().is3xxRedirection());
    }
}