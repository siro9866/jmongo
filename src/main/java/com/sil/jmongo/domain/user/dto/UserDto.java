package com.sil.jmongo.domain.user.dto;

import com.sil.jmongo.domain.user.entity.User;
import com.sil.jmongo.global.code.RoleCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

import static com.sil.jmongo.global.validation.ValidationPatterns.EMAIL_FORMAT;

public class UserDto {

    @Getter
    @Setter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {

        @NotBlank @Size(max = 20)
        private String username;    // 아이디

        @NotBlank @Size(max = 20)
        private String password;    // 비밀번호

        @NotBlank @Size(max = 50)
        private String name;        // 이름

        @NotBlank
        @Pattern(regexp = EMAIL_FORMAT, message = "{validation.email}")
        private String email;       // 이메일

        private String role = RoleCode.ROLE_USER.name();        // 롤

        public User toEntity() {
            return User.builder()
                    .username(username)
                    .password(password)
                    .name(name)
                    .email(email)
                    .role(role)
                    .joinAt(LocalDateTime.now())
                    .build();
        }
    }

    @Getter
    @Setter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String id;
        private String username;    // 아이디
        private String password;    // 비밀번호
        private String name;        // 이름
        private String email;       // 이메일
        private String role;        // 롤
        private LocalDateTime joinAt; // 가입일시
        private LocalDateTime signAt; // 로그인일시
        private boolean enabled;        // 로그인가능

        private String createdBy;
        private LocalDateTime createdAt;
        private String modifiedBy;
        private LocalDateTime modifiedAt;

        public static Response toDto(User user) {
            return Response.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .name(user.getName())
                    .email(user.getEmail())
                    .role(user.getRole())
                    .joinAt(user.getJoinAt())
                    .signAt(user.getSignAt())
                    .createdBy(user.getCreatedBy())
                    .createdAt(user.getCreatedAt())
                    .modifiedBy(user.getModifiedBy())
                    .modifiedAt(user.getModifiedAt())
                    .enabled(user.isEnabled())
                    .build();
        }
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginRequest {
        private String username;
        private String password;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginResponse {
        private String token;
        private String username;
        private String role;

        public User toEntity() {
            return User.builder()
                    .username(username)
                    .role(role)
                    .build();
        }

    }
}