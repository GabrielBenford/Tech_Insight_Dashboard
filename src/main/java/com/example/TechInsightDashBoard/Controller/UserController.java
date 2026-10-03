package com.example.TechInsightDashBoard.Controller;


import com.example.TechInsightDashBoard.DTO.UserDTO.UserRequestDTO;
import com.example.TechInsightDashBoard.DTO.UserDTO.UserResponseDTO;
import com.example.TechInsightDashBoard.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/Dashboard/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public UserResponseDTO getUserById(@PathVariable Long id) {
        return userService.findUserById(id);
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }

    @PutMapping("/{id}")
    public UserResponseDTO updateUser(@PathVariable Long id, @Valid @RequestBody UserRequestDTO user) {
        return userService.updateUser(id, user);
}

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}