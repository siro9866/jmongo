package com.sil.jmongo.domain.user.service;

import com.sil.jmongo.domain.user.dto.UserDto;
import com.sil.jmongo.domain.user.entity.User;
import com.sil.jmongo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDto.Response createUser(UserDto.CreateRequest userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        // 엔티티로 변환하기 전에 비밀번호 암호화
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User savedUser = userRepository.save(userDto.toEntity());
        return UserDto.Response.toDto(savedUser);
    }

    public UserDto.Response getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return UserDto.Response.toDto(user);
    }

    public Page<UserDto.Response> getPagedUsers(int page, int size, String sortField, boolean isDesc) {
        Sort.Direction direction = isDesc ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        Page<User> userPage = userRepository.findAll(pageable);
        return userPage.map(UserDto.Response::toDto);
    }


    // 사용자전체 조회하기
    public List<UserDto.Response> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDto.Response::toDto)
                .collect(Collectors.toList());
    }
}