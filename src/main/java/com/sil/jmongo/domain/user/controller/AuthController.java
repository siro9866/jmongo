package com.sil.jmongo.domain.user.controller;

import com.sil.jmongo.domain.user.dto.UserDto;
import com.sil.jmongo.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Auth API")
public class AuthController {

    private final UserService userService;

    @Operation(summary = "회원가입", description = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<UserDto.Response> signup(@ParameterObject @ModelAttribute @Valid UserDto.CreateRequest request) {
        // SWAGGER에 form타입으로 나오려고 위처럼변경함 @RequestBody @Valid UserDto.CreateRequest
        log.debug(request.toString());
        UserDto.Response response = userService.createUser(request);
        return ResponseEntity.ok(response);
    }

//    @Operation(summary = "로그인", description = "사용자 로그인")
//    @PostMapping("/login")
//    public ResponseEntity<Void> login(@RequestBody UserDto.LoginRequest request) {
//        // Spring Security에서 처리하므로 여기선 더미 응답
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//    }
//
//    @Operation(summary = "로그아웃", description = "사용자 로그아웃")
//    @PostMapping("/logout")
//    public ResponseEntity<Void> logout() {
//        return ResponseEntity.ok().build();
//    }

}