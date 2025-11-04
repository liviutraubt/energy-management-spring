package org.example.userservice.service;

import lombok.RequiredArgsConstructor;
import org.example.userservice.entity.UserEntity;
import org.example.userservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataInit implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if(!userRepository.existsById(1L)) {
            userRepository.save(UserEntity.builder()
                            .id(1L)
                            .firstName("Admin")
                            .lastName("Admin")
                            .address("Strada Baritiu")
                            .email("admin@admin.com")
                            .telephone("0712345678")
                    .build());
        }
    }
}
