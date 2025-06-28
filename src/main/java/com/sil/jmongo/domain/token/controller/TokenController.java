package com.sil.jmongo.domain.token.controller;

import com.sil.jmongo.domain.token.service.TokenService;
import com.sil.jmongo.global.util.UtilCommon;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 *
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "토큰컨트롤러", description = "토큰컨트롤러")
public class TokenController {

    private final TokenService tokenService;

    /**
     * 토큰 재발급
     * 리프레시토큰을 활요하여 토큰 재발급
     */
    @Operation(summary = "토큰 재발급", description = "토큰 재발급")
    @PostMapping("/reIssue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Map<String, String> result = tokenService.reissue(request, response);
        response.setHeader("accessToken", result.get("accessToken"));					// access 토큰은 헤더로 내려줌
        response.addCookie(UtilCommon.createCookie("refreshToken", result.get("refreshToken")));	// refresh 토큰은 쿠키에 저장
        return ResponseEntity.ok(null);
    }
}
