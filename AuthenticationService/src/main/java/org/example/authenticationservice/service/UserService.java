package org.example.authenticationservice.service;

import jdk.jshell.spi.ExecutionControl;
import lombok.RequiredArgsConstructor;
import org.example.authenticationservice.dto.LoginRequest;
import org.example.authenticationservice.dto.RegisterRequest;
import org.example.authenticationservice.dto.TokenResponse;
import org.example.authenticationservice.dto.UserDTO;
import org.example.authenticationservice.entity.Roles;
import org.example.authenticationservice.entity.UserEntity;
import org.example.authenticationservice.mapper.UserMapper;
import org.example.authenticationservice.repository.UserRepository;
import org.example.authenticationservice.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public Long registerUser(RegisterRequest registerRequest) {
        if(userRepository.existsByUsername(registerRequest.username())){
            throw new RuntimeException("Username already exists");
        }

        var user = UserEntity.builder()
                .username(registerRequest.username())
                .password(encoder.encode(registerRequest.password()))
                .role(Roles.USER)
                .build();

        userRepository.save(user);
        return user.getId();
    }

    public TokenResponse login(LoginRequest loginRequest) {
        var user = userRepository.findByUsername(loginRequest.username()).orElseThrow(() -> new RuntimeException("Username not found"));
        if(!encoder.matches(loginRequest.password(), user.getPassword())){
            throw new RuntimeException("Passwords don't match");
        }
        return tokensFor(user);
    }

    private TokenResponse tokensFor(UserEntity user) {
        var access = jwt.generateAccess(user.getUsername(), String.valueOf(user.getRole()));
        return new TokenResponse(access);
    }
}
