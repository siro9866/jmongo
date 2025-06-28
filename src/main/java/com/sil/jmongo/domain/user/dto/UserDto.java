package com.sil.jmongo.domain.user.dto;

import com.sil.jmongo.domain.user.entity.User;
import com.sil.jmongo.global.code.RoleCode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

import static com.sil.jmongo.global.validation.ValidationPatterns.EMAIL_FORMAT;

public class UserDto {

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

    /**
     * 등록
     */
    @Getter
    @Setter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {

        @Schema(description = "아이디")
        @NotBlank @Size(max = 20)
        private String username;    // 아이디

        @Schema(description = "비밀번호", example = "1234")
        @NotBlank @Size(max = 20)
        private String password;    // 비밀번호

        @NotBlank @Size(max = 50)
        private String name;        // 이름

        @NotBlank
        @Pattern(regexp = EMAIL_FORMAT, message = "{validation.email}")
        private String email;       // 이메일

        @Schema(description = "권한", example = "ROLE_USER")
        private String role = RoleCode.ROLE_USER.name();        // 롤

        public User toEntity() {
            return User.builder()
                    .username(username)
                    .password(password)
                    .name(name)
                    .email(email)
                    .role(role)
                    .joinedAt(LocalDateTime.now())
                    .build();
        }
    }

    /**
     * 수정
     */
    @Getter
    @Setter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModifyRequest {

        @NotBlank @Size(max = 50)
        private String name;        // 이름

        @NotBlank
        @Pattern(regexp = EMAIL_FORMAT, message = "{validation.email}")
        private String email;       // 이메일

        public void modifyUser(User user) {
            user.setName(this.name);
            user.setEmail(this.email);
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
        private LocalDateTime joinedAt; // 가입일시
        private LocalDateTime signedAt; // 로그인일시
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
                    .joinedAt(user.getJoinedAt())
                    .signedAt(user.getSignedAt())
                    .createdBy(user.getCreatedBy())
                    .createdAt(user.getCreatedAt())
                    .modifiedBy(user.getModifiedBy())
                    .modifiedAt(user.getModifiedAt())
                    .enabled(user.isEnabled())
                    .build();
        }
    }


    /**
     * 조회조건
     */
    @Getter
    @Setter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Search {

        @Schema(description = "아이디, 이름, 이메일 중 하나")
        private String keyword;

        @Schema(description = "등록일 시작 (yyyyMMdd)", example = "20250101")
        private String fromDate;

        @Schema(description = "등록일 종료 (yyyyMMdd)", example = "20301231")
        private String toDate;

        @Schema(description = "페이지 번호 (0부터 시작)", example = "0", defaultValue = "0")
        private int page = 0;

        @Schema(description = "페이지 크기", example = "10", defaultValue = "10")
        private int size = 10;

        @Schema(description = "정렬 기준 필드", example = "createdAt", defaultValue = "createdAt")
        private String sortBy = "createdAt";

        @Schema(description = "내림차순 여부", example = "true", defaultValue = "true")
        private boolean desc = true;
    }

}