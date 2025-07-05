package com.sil.jmongo.domain.user.service;

import com.sil.jmongo.domain.user.dto.UserDto;
import com.sil.jmongo.domain.user.entity.Users;
import com.sil.jmongo.domain.user.repository.UserRepository;
import com.sil.jmongo.global.exception.CustomException;
import com.sil.jmongo.global.response.ResponseCode;
import com.sil.jmongo.global.util.UtilCommon;
import com.sil.jmongo.global.util.UtilMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
            query.addCriteria(Criteria.where("createdAt").gte(from).lte(to));
        }

        // 📦 페이징 + 정렬
        Sort.Direction direction = search.isDesc() ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(search.getPage(), search.getSize(), Sort.by(direction, search.getSortBy()));
        query.with(pageable);

        // ✨ 실행
        List<Users> users = mongoTemplate.find(query, Users.class);
        long total = mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Users.class);

        // DTO 변환
        List<UserDto.Response> content = users.stream()
                .map(UserDto.Response::toDto)
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageable, total);
    }

    /**
     * 상세
     * @param id
     * @return
     */
    public UserDto.Response detailUser(String id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("notfound.data", null)));
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
        Users savedUser = userRepository.save(request.toEntity());
        return UserDto.Response.toDto(savedUser);
    }

    /**
     * 수정
     * @param id
     * @param request
     */
    public void modifyUser(String id, UserDto.ModifyRequest request) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("notfound.data", null)));
        request.modifyUser(user);

        // MongoDB에 명시적으로 저장
        userRepository.save(user);
    }

    /**
     * 삭제
     * @param id
     */
    public void deleteUser(String id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("notfound.data", null)));
        userRepository.deleteById(user.getId());
    }
}