package com.example.TechInsightDashBoard.Controller;

import com.example.TechInsightDashBoard.DTO.AuthDTO.LoginDTO;
import com.example.TechInsightDashBoard.DTO.UserDTO.UserRequestDTO;
import com.example.TechInsightDashBoard.Entity.UserEntity;
import com.example.TechInsightDashBoard.Service.TokenService;
import com.example.TechInsightDashBoard.Service.UserService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

     private final AuthenticationManager authenticationManager;
     private final TokenService tokenService;
     private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginDTO loginDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.password())
        );

        var token= tokenService.creatingToken(UserEntity.class.cast(authentication.getPrincipal()));
        return ResponseEntity.ok(token);

    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid UserRequestDTO userRequestDTO) {
        userService.createUser(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

}