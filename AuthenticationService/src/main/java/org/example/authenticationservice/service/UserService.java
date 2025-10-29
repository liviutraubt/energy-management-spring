package org.example.authenticationservice.service;

import lombok.RequiredArgsConstructor;
import org.example.authenticationservice.dto.LoginRequest;
import org.example.authenticationservice.dto.RegisterRequest;
import org.example.authenticationservice.dto.UserDTO;
import org.example.authenticationservice.entity.Roles;
import org.example.authenticationservice.entity.UserEntity;
import org.example.authenticationservice.mapper.UserMapper;
import org.example.authenticationservice.repository.UserRepository;
import org.example.authenticationservice.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public String login(LoginRequest loginRequest) {
        var user = userRepository.findByUsername(loginRequest.username()).orElseThrow(() -> new RuntimeException("Username not found"));
        if(!encoder.matches(loginRequest.password(), user.getPassword())){
            throw new RuntimeException("Passwords don't match");
        }
        var access = jwt.generateAccess(user.getUsername(), String.valueOf(user.getRole()), user.getId());
        return access;
    }

    public List<UserDTO> getUsers() {return userMapper.userEntityToUserDTO(userRepository.findAll());}

    public Long deleteUser(Long id){
        if(userRepository.existsById(id)){
            userRepository.deleteById(id);
        }
        else {
            throw new RuntimeException("User not found");
        }
        return id;
    }
}
