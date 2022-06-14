package com.example.dits.controllers;

import com.example.dits.DAO.StatisticRepository;
import com.example.dits.entity.Statistic;
import com.example.dits.entity.Topic;
import com.example.dits.service.TopicService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
public class AdminStatisticControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TopicService topicService;

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
