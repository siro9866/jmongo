package com.sil.jmongo.domain.user.controller;

import com.sil.jmongo.domain.user.dto.UserDto;
import com.sil.jmongo.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    public ResponseEntity<Page<UserDto.Response>> listUser(@ParameterObject @ModelAttribute UserDto.Search search) {
        Page<UserDto.Response> users = userService.listUser(search);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "회원상세", description = "회원상세")
    @GetMapping("/{id}")
    public ResponseEntity<UserDto.Response> detailUser(@PathVariable String id) {
        UserDto.Response user = userService.detailUser(id);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "회원수정", description = "회원수정")
    @PutMapping("/{id}")
    public ResponseEntity<UserDto.Response> modifyUser(@PathVariable String id
    , @ParameterObject @ModelAttribute @Valid UserDto.ModifyRequest request) {
        userService.modifyUser(id, request);
        return ResponseEntity.ok(null);
    }

    @Operation(summary = "회원삭제", description = "회원삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<UserDto.Response> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(null);
    }
}