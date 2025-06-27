package com.sil.jmongo.global.config;

import com.sil.jmongo.domain.user.dto.UserDto;
import com.sil.jmongo.domain.user.entity.User;
import com.sil.jmongo.domain.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(UserRepository userRepository) {
        LocalDateTime now = LocalDateTime.now();
        return args -> {
            // 이미 데이터가 있으면 스킵
            if (userRepository.count() == 0) {
                UserDto.CreateRequest createRequest = new UserDto.CreateRequest();
                createRequest.setUsername("admin");
                createRequest.setPassword("$2a$10$NJ3uaZgreRYa5h.LUKdNZu4O0PrRXn/INTtS8eaBny4eNjEviQu46");
                createRequest.setName("관리자");
                createRequest.setEmail("admin@member.com");
                createRequest.setRole("ROLE_ADMIN");
                createRequest.setJoinAt(now);

                userRepository.saveAll(List.of(createRequest.toEntity()));
                System.out.println("✅ 초기 유저 데이터 등록 완료");
            }
        };
    }
}
