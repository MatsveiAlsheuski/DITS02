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
    private final RoleService roleService;
    private final UserMapper userMapper;

    @GetMapping("/usersList")
    public String usersList(HttpSession session, ModelMap model) {
        session.setAttribute("user", userService.getUserByLogin(getPrincipal()));
        model.addAttribute("title", "Users list");
        return "admin/usersList";
    }

    @ResponseBody
    @GetMapping("/getUsers")
    private List<UserInfoDTO> getUsers() {
        return getUsersFromDB();
    }

    @ResponseBody
    @PostMapping("/addUser")
    public List<UserInfoDTO> addUser(@RequestParam String firstName, @RequestParam String surname,
                                     @RequestParam String role, @RequestParam String login,
                                     @RequestParam String password) {
        if (checkNullableParameters(firstName, surname, role, login, password)) {
            User user = fillOutUser(firstName, surname, role, login, password);
            userService.save(user);
        }
        return getUsersFromDB();
    }

    @ResponseBody
    @PutMapping("/editUser")
    public List<UserInfoDTO> editUser(@RequestParam int userId, @RequestParam String firstName,
                                      @RequestParam String surname, @RequestParam String role,
                                      @RequestParam String login, @RequestParam String password) {
        if (checkNullableParameters(firstName, surname, role, login, password)) {
            User user = fillOutUser(firstName, surname, role, login, password);
            userService.update(user,userId);
        }
        return getUsersFromDB();
    }

    @ResponseBody
    @DeleteMapping("/removeUser")
    public List<UserInfoDTO> removeUser(@RequestParam int userId) {
        User user = userService.getUserByUserId(userId);
        userService.delete(user);
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

    private User fillOutUser(String firstName, String surname, String role, String login, String password) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(surname);
        user.setRole(roleService.getRoleByRoleName(role));
        user.setLogin(login);
        user.setPassword(password);
        return user;
    }

    private boolean checkNullableParameters(String firstName, String surname, String role, String login, String password) {
        if (firstName == null) return false;
        if (surname == null) return false;
        if (!role.equals("ROLE_USER") && !role.equals("ROLE_ADMIN")) return false;
        if (login == null) return false;
        if (password == null) return false;
        return true;
    }
}