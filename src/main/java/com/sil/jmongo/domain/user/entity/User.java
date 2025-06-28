package com.sil.jmongo.domain.user.entity;

import com.sil.jmongo.global.entity.Base;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User extends Base {

    @Id
    private String id;
    @Indexed(unique = true)
    private String username;    // 아이디
    private String password;    // 비밀번호
    private String name;        // 이름
    private String email;       // 이메일
    private String role;        // 롤
    private LocalDateTime joinAt; // 가입일시
    private LocalDateTime signAt; // 로그인일시
    @Builder.Default
    private boolean enabled = true;        // 로그인가능

}