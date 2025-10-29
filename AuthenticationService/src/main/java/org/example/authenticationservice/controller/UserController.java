package org.example.authenticationservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.authenticationservice.dto.LoginRequest;
import org.example.authenticationservice.dto.RegisterRequest;
import org.example.authenticationservice.dto.UserDTO;
import org.example.authenticationservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        try{
            return ResponseEntity.ok(userService.registerUser(registerRequest));
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try{
            return ResponseEntity.ok(Map.of("token", userService.login(loginRequest)));
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/getall")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> userList = userService.getUsers();
        return ResponseEntity.ok(userList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try{
            return ResponseEntity.ok(userService.deleteUser(id));
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/register-admin")
    public ResponseEntity<?> registerAdmin(@RequestBody RegisterRequest registerRequest) {
        try{
            return ResponseEntity.ok(userService.adminRegisterUser(registerRequest));
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
