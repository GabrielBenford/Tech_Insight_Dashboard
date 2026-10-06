package com.example.TechInsightDashBoard.Mapper;

import com.example.TechInsightDashBoard.DTO.UserDTO.UserResponseDTO;
import com.example.TechInsightDashBoard.Entity.UserEntity;

public class UserMapper {

    public static UserResponseDTO toUserResponseDTO(UserEntity userEntity) {
        return new UserResponseDTO(userEntity.getId(), userEntity.getDisplayName(), userEntity.getEmail());
    }

}
