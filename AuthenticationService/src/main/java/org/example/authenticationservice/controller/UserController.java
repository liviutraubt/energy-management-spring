package org.example.authenticationservice.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.authenticationservice.dto.LoginRequest;
import org.example.authenticationservice.dto.RegisterRequest;
import org.example.authenticationservice.dto.UserDTO;
import org.example.authenticationservice.security.JwtTokenService;
import org.example.authenticationservice.security.annotations.AllowAdmin;
import org.example.authenticationservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;
    private  final JwtTokenService jwtTokenService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        try{
            return ResponseEntity.ok(userService.registerUser(registerRequest));
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/healthcheck")
    public ResponseEntity<?> checkHealth() {
        return ResponseEntity.status(200).build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request, HttpServletResponse response) {
        UserDTO user = userService.login(request);
        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error","Invalid credentials"));
        }

        String jwt = jwtTokenService.createJwtToken(user.username(), user.role(), user.id());
        Cookie cookie = new Cookie("auth-cookie",jwt);
        cookie.setPath("/");
        cookie.setDomain("localhost");
        cookie.setHttpOnly(false);
        response.addCookie(cookie);
        return ResponseEntity.ok(Map.of("token", jwt));
    }

    @GetMapping("/getall")
    @AllowAdmin
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> userList = userService.getUsers();
        return ResponseEntity.ok(userList);
    }

    @DeleteMapping("/{id}")
    @AllowAdmin
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try{
            return ResponseEntity.ok(userService.deleteUser(id));
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/register-admin")
    @AllowAdmin
    public ResponseEntity<?> registerAdmin(@RequestBody RegisterRequest registerRequest) {
        try{
            return ResponseEntity.ok(userService.adminRegisterUser(registerRequest));
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
