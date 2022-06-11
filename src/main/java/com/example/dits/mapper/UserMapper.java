package com.example.dits.mapper;

import com.example.dits.dto.RoleDTO;
import com.example.dits.dto.UserInfoDTO;
import com.example.dits.entity.Role;
import com.example.dits.entity.User;
import com.example.dits.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class UserMapper {

    private final RoleService roleService;

    public UserInfoDTO convertToUserDTO(User user){
        List<String> roles = roleService.getAllRoles();
        return UserInfoDTO.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .login(user.getLogin())
                .password(user.getPassword())
                .role(convertToRoleDTO(roles,user.getRole()))
                .build();
    }

    private RoleDTO convertToRoleDTO(List<String> roles,Role role){
        return new RoleDTO(roles,role.getRoleName());
    }
}
