package com.example.dits.controllers;

import com.example.dits.dto.*;
import com.example.dits.entity.Statistic;
import com.example.dits.entity.Topic;
import com.example.dits.entity.User;
import com.example.dits.mapper.TestStatisticByUserMapper;
import com.example.dits.mapper.UserMapper;
import com.example.dits.service.TopicService;
import com.example.dits.service.UserService;
import com.example.dits.service.impl.StatisticServiceImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminStatisticController {

    private final ModelMapper modelMapper;
    private final UserMapper userMapper;
    private final StatisticServiceImpl statisticService;
    private final TopicService topicService;
    private final UserService userService;
    private final TestStatisticByUserMapper testStatisticByUserMapper;

    @GetMapping("/adminStatistic")
    public String testStatistic(ModelMap model){

        List<TopicDTO> topicDTOList = topicService.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
        model.addAttribute("topicList",topicDTOList);
        model.addAttribute("title","Statistic");
        return "admin/test-statistic";
    }

    @ResponseBody
    @GetMapping("/getTestsStatistic")
    public List<TestStatistic> getTestsStatistics(@RequestParam int id) {
        return statisticService.getListOfTestsWithStatisticsByTopic(id);
    }

    @GetMapping("/getUserStatistic")
    public String userStatistic(ModelMap model){
        List<UserInfoDTO> userListStat = getUsersFromDB();
        model.addAttribute("userListStat", userListStat);
        return "admin/user-statistic";
    }

    @ResponseBody
    @GetMapping("/getUserTestsStatistic")
    public List<TestStatisticByUser> userTestsStatistic(@RequestParam int id){
        return userTestsStatisticDTO(id);
    }

    @ResponseBody
    @GetMapping("/adminStatistic/removeStatistic/byId")
    public String removeStatisticByUserId(@RequestParam int id){
        statisticService.removeStatisticByUserId(id);
        return "success";
    }

    @GetMapping("/adminStatistic/removeStatistic/all")
    public String removeAllStatistic(){
        statisticService.deleteAll();
        return "redirect:/admin/adminStatistic";
    }

    private TopicDTO convertToDTO(Topic topic){
        return modelMapper.map(topic, TopicDTO.class);
    }

    private List<UserInfoDTO> getUsersFromDB() {
        List<User> userList = userService.findAll().stream().
                filter(user -> user.getRole().getRoleName().equals("ROLE_USER"))
                .collect(Collectors.toList());
        return userList.stream().map(userMapper::convertToUserDTO).collect(Collectors.toList());
    }

    private List<TestStatisticByUser> userTestsStatisticDTO(int id){
        User user = userService.getUserByUserId(id);
        List<Statistic> testList = statisticService.getStatisticsByUser(user);
        return testStatisticByUserMapper.convertListToTestStatisticByUser(testList);
    }
}