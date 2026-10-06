package com.example.TechInsightDashBoard.Service;

import com.example.TechInsightDashBoard.DTO.UserDTO.UserRequestDTO;
import com.example.TechInsightDashBoard.DTO.UserDTO.UserResponseDTO;
import com.example.TechInsightDashBoard.Entity.UserEntity;
import com.example.TechInsightDashBoard.Mapper.UserMapper;
import com.example.TechInsightDashBoard.Repository.UserRepository;
import com.example.TechInsightDashBoard.exception.EmailAlreadyInUseException;
import com.example.TechInsightDashBoard.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserResponseDTO findUserById(Long id) {
        return userRepository.findById(id)
                .map(UserMapper::toUserResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: ", id));

    }

    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        String email = userRequestDTO.email().trim();
        if (userRepository.findByEmailIgnoreCase(email).isPresent()) {
            throw new EmailAlreadyInUseException(email);
        }
        UserEntity userEntity = UserEntity.builder()
                .username(userRequestDTO.name().trim())
                .email(email)
                .password(passwordEncoder.encode(userRequestDTO.password()))
                .build();
        return UserMapper.toUserResponseDTO(userRepository.save(userEntity));
    }

    public UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        String email = userRequestDTO.email().trim();
        userRepository.findByEmailIgnoreCase(email)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new EmailAlreadyInUseException(email);
                });

        userEntity.setUsername(userRequestDTO.name().trim());
        userEntity.setEmail(email);
        userEntity.setPassword(passwordEncoder.encode(userRequestDTO.password()));
        return UserMapper.toUserResponseDTO(userEntity);
    }

    public void deleteUser(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        userRepository.delete(userEntity);
    }
}
