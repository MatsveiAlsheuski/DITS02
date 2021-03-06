package com.example.dits.service.impl;

import com.example.dits.DAO.TopicRepository;
import com.example.dits.entity.Topic;
import com.example.dits.service.TopicService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class TopicServiceImplTest {
    @Autowired
    private TopicService service;
    @MockBean
    private TopicRepository repository;

    private Topic topic;

    @Test
    void shouldInvokeMethodSaveFromRepository() {
       service.save(topic);
       verify(repository, times(1)).save(topic);
       verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldReturnFromUpdateWhenThereIsNoTopicById() {
        when(repository.findById(anyInt())).thenReturn(Optional.empty());
        service.update(topic,anyInt());
        verify(repository,times(1)).findById(anyInt());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldInvokeSaveOnRepositoryWhenThereIsTopicById(){
        when(repository.findById(anyInt())).thenReturn(Optional.of(new Topic()));
        service.update(topic,anyInt());
        verify(repository,times(1)).findById(anyInt());
        verify(repository,times(1)).save(topic);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldInvokeDeleteOnRepository(){
        service.delete(topic);
        verify(repository,times(1)).delete(topic);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldInvokeFindAllOnRepository(){
        service.findAll();
        verify(repository,times(1)).findAll();
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldInvokeGetTopicByName(){
        String anyString = anyString();
        service.getTopicByName(anyString);
        verify(repository,times(1)).getTopicByName(anyString);
        verifyNoMoreInteractions(repository);
    }

}