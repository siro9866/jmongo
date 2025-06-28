package com.sil.jmongo.domain.user.controller;

import com.sil.jmongo.domain.user.dto.UserDto;
import com.sil.jmongo.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Page<UserDto.Response>> getUserList(@ParameterObject @ModelAttribute UserDto.Search search) {
        Page<UserDto.Response> users = userService.getUserList(search);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "회원상세", description = "회원상세")
    @GetMapping("/{username}")
    public ResponseEntity<UserDto.Response> getUserDetail(@PathVariable String username) {
        UserDto.Response user = userService.getUserDetail(username);
        return ResponseEntity.ok(user);
    }
}