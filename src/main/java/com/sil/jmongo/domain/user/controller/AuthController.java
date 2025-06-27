package com.sil.jmongo.domain.user.controller;

import com.sil.jmongo.domain.user.dto.UserDto;
import com.sil.jmongo.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
//
//    @GetMapping("/login")
//    public String loginPage() {
//        return "login";
//    }
//
//    @PostMapping("/login")
//    @ResponseBody
//    public ResponseEntity<UserDto.LoginResponse> login(@RequestBody @Valid UserDto.LoginRequest loginRequest) {
//        UserDto.LoginResponse response = userService.login(loginRequest);
//        return ResponseEntity.ok(response);
//    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<UserDto.Response> register(@RequestBody @Valid UserDto.CreateRequest userRequest) {
        UserDto.Response response = userService.createUser(userRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }
}