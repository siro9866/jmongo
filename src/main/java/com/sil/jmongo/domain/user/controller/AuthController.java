package com.sil.jmongo.domain.user.controller;

import com.sil.jmongo.domain.user.dto.UserDto;
import com.sil.jmongo.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Auth API")
public class AuthController {

    private final UserService userService;

    @Operation(summary = "회원가입", description = "회원가입")
    @PostMapping("/register")
    public ResponseEntity<UserDto.Response> register(@RequestBody @Valid UserDto.CreateRequest userRequest) {
        UserDto.Response response = userService.createUser(userRequest);
        return ResponseEntity.ok(response);
    }
}