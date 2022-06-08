package com.example.dits.controllers;

import com.example.dits.dto.UserInfoDTO;
import com.example.dits.entity.User;
import com.example.dits.mapper.UserMapper;
import com.example.dits.service.RoleService;
import com.example.dits.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminHomeController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/usersList")
    public String getUsersList(ModelMap model, HttpSession session) {
        List<UserInfoDTO> userDTOList = getUsersFromDB();
        session.setAttribute("user", userService.getUserByLogin(getPrincipal()));
        model.addAttribute("title", "Users list");
        model.addAttribute("userDTOList", userDTOList);
        return "admin/usersList";
    }

    @ResponseBody
    @GetMapping("/getUsers")
    private List<UserInfoDTO> getUsers() {
        return getUsersFromDB();
    }

    private static String getPrincipal() {
        String userName;
        Object principal = SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else
            userName = principal.toString();
        return userName;
    }

    private List<UserInfoDTO> getUsersFromDB() {
        List<User> userList = userService.findAll();
        return userList.stream().map(userMapper::convertToUserDTO).collect(Collectors.toList());
    }
}