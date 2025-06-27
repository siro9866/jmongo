package com.sil.jmongo.domain.user.controller;

import com.sil.jmongo.domain.user.dto.UserDto;
import com.sil.jmongo.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String getUsersPage(Model model) {
        List<UserDto.Response> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/{username}")
    @ResponseBody
    public ResponseEntity<UserDto.Response> getUserByUsername(@PathVariable String username) {
        UserDto.Response user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/profile")
    public String getProfilePage(@RequestParam(required = false) String username, Model model) {
        if (username == null) {
            return "redirect:/api/auth/login";
        }
        UserDto.Response user = userService.getUserByUsername(username);
        model.addAttribute("user", user);
        return "profile";
    }
}