package com.example.dits.controllers;

import com.example.dits.dto.QuestionEditModel;
import com.example.dits.entity.Topic;
import com.example.dits.mapper.QuestionMapper;
import com.example.dits.mapper.TestMapper;
import com.example.dits.service.*;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
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
public class AdminTestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TopicService topicService;
    @MockBean
    private QuestionMapper questionMapper;

    @Test
    public void testGetTopicsOnAdmin() throws Exception {
        mockMvc.perform(get("/admin/testBuilder"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetTopicListOnAdmin() throws Exception {
        mockMvc.perform(get("/admin/getTopics"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetTestsWithQuestionsOnAdmin() throws Exception {
        mockMvc.perform(get("/admin/getTests").param("id","0"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetQuestionsWithAnswersOnAdmin() throws Exception {
        mockMvc.perform(get("/admin/getAnswers").param("id","1").with(csrf()))
                .andExpect(status().isOk());
    }


    @Test
    public void testRemoveTopicOnAdmin() throws Exception {
        mockMvc.perform(delete("/admin/removeTopic").param("topicId","0").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddTopicOnAdmin() throws Exception {
        mockMvc.perform(post("/admin/addTopic").param("name","0").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    public void testEditTopicOnAdmin() throws Exception {
        mockMvc.perform(put("/admin/editTopic").param("id","1").param("name","1").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetRolesOnAdmin() throws Exception {
        mockMvc.perform(get("/admin/getRoles"))
                .andExpect(status().isOk());
    }
}