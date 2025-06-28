package com.sil.jmongo.domain.user.controller;

import com.sil.jmongo.domain.user.dto.UserDto;
import com.sil.jmongo.domain.user.service.UserService;
import com.sil.jmongo.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "회원", description = "회원 API")
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원목록", description = "회원목록")
    @GetMapping
//    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<UserDto.Response>> getUsersPage() {
        List<UserDto.Response> users = userService.getAllUsers();
        return new ApiResponse<>(users);
    }

    @Operation(summary = "회원상세", description = "회원상세")
    @GetMapping("/{username}")
    public ApiResponse<UserDto.Response> getUserByUsername(@PathVariable String username) {
        UserDto.Response user = userService.getUserByUsername(username);
        return new ApiResponse<>(user);
    }
}