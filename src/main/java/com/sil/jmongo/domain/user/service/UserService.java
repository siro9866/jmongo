package com.sil.jmongo.domain.user.service;

import com.sil.jmongo.domain.user.dto.UserDto;
import com.sil.jmongo.domain.user.entity.User;
import com.sil.jmongo.domain.user.repository.UserRepository;
import com.sil.jmongo.global.util.UtilCommon;
import com.sil.jmongo.global.util.UtilMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final MongoTemplate mongoTemplate;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UtilMessage utilMessage;


    /**
     * 목록
     * @param search
     * @return
     */
    public Page<UserDto.Response> listUser(UserDto.Search search) {
        Query query = new Query();

        // 🔍 키워드 like 검색 (username, email, name 중 하나라도 포함)
        if (UtilCommon.isNotEmpty(search.getKeyword())) {
            String keyword = search.getKeyword();
            Criteria keywordCriteria = new Criteria().orOperator(
                    Criteria.where("username").regex(keyword, "i"),
                    Criteria.where("email").regex(keyword, "i"),
                    Criteria.where("name").regex(keyword, "i")
            );
            query.addCriteria(keywordCriteria);
        }

        // 📅 가입일자 조건
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        if (UtilCommon.isNotEmpty(search.getFromDate()) && UtilCommon.isNotEmpty(search.getToDate())) {
            LocalDateTime from = LocalDate.parse(search.getFromDate(), formatter).atStartOfDay();
            LocalDateTime to = LocalDate.parse(search.getToDate(), formatter).atTime(23, 59, 59);
            query.addCriteria(Criteria.where("joinAt").gte(from).lte(to));
        }

        // 📦 페이징 + 정렬
        Sort.Direction direction = search.isDesc() ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(search.getPage(), search.getSize(), Sort.by(direction, search.getSortBy()));
        query.with(pageable);

        // ✨ 실행
        List<User> users = mongoTemplate.find(query, User.class);
        long total = mongoTemplate.count(Query.of(query).limit(-1).skip(-1), User.class);

        // DTO 변환
        List<UserDto.Response> content = users.stream()
                .map(UserDto.Response::toDto)
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageable, total);

    }

    /**
     * 상세
     * @param username
     * @return
     */
    public UserDto.Response detailUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(utilMessage.getMessage("notfound.data.one", new String[] { username })));
        return UserDto.Response.toDto(user);
    }

    /**
     * 등록
     * @param request
     * @return
     */
    public UserDto.Response createUser(UserDto.CreateRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException(utilMessage.getMessage("duplicate.username", null));
        }

        // 엔티티로 변환하기 전에 비밀번호 암호화
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        User savedUser = userRepository.save(request.toEntity());
        return UserDto.Response.toDto(savedUser);
    }

    /**
     * 수정
     * @param username
     * @param request
     */
    public void modifyUser(String username, UserDto.ModifyRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(utilMessage.getMessage("notfound.data.one", new String[] { username })));
        request.modifyUser(user);

        // MongoDB에 명시적으로 저장
        userRepository.save(user);
    }

    /**
     * 삭제
     * @param username
     */
    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(utilMessage.getMessage("notfound.data.one", new String[] { username })));
        userRepository.deleteById(user.getId());
    }



    // 사용자전체 조회하기
//    public List<UserDto.Response> getAllUsers() {
//        return userRepository.findAll().stream()
//                .map(UserDto.Response::toDto)
//                .collect(Collectors.toList());
//    }
}