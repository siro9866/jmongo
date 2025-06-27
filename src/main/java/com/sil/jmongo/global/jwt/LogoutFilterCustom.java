package com.sil.jmongo.global.jwt;

import com.sil.jmongo.domain.token.repository.TokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

/**
 * 로그아웃
 */
public class LogoutFilterCustom extends GenericFilterBean {

	private final JwtUtil jwtUtil;
	private final TokenRepository tokenRepository;
	
	public LogoutFilterCustom(JwtUtil jwtUtil, TokenRepository tokenRepository) {
		this.jwtUtil = jwtUtil;
		this.tokenRepository = tokenRepository;
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
	}

	private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		// 로그아웃 url 인지 확인
		String requestUri = request.getRequestURI();
		if(!requestUri.matches("^/logout$")) {
			chain.doFilter(request, response);
			return;
		}
		
		// 요청 method 확인
		String requestMethod = request.getMethod();
		if(!requestMethod.equalsIgnoreCase("POST")) {
			chain.doFilter(request, response);
			return;
		}
		
		// refresh 토큰을 얻는다
		String refreshToken = null;
		Cookie[] cookies = request.getCookies();
		for(Cookie cookie : cookies) {
			if(cookie.getName().equals("refreshToken")) {
				refreshToken = cookie.getValue();
			}
		}
		
		// refresh 토큰 널체크
		if(refreshToken == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		// refresh 토큰 만료 체크
		try {
			jwtUtil.isExpired(refreshToken);
		} catch (ExpiredJwtException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		// 토큰이 refresh인지 확인(발급시 페이로드에 넣었음)
		String tokenCategory = jwtUtil.getTokenCategory(refreshToken);
		if(!tokenCategory.equals("refreshToken")) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		// DB에 해당 refresh 토큰이 있는지 확인
		Boolean isExist = tokenRepository.existsByRefreshToken(refreshToken);
		if(!isExist) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		// 휴.. 이제 로그아웃 진행
		// refresh 토큰 DB에서 제거
		tokenRepository.deleteByRefreshToken(refreshToken);
		
		// refresh 토큰 Cookie 값 지우고 생명을 꺼버림 으로 밀어버림
		Cookie cookie = new Cookie("refreshToken", null);
		cookie.setMaxAge(0);	// 생명주기
//		cookie.setSecure(true);	// https 사용할 경우
		cookie.setPath("/");	// 쿠키가 적용될 범위

		response.addCookie(cookie);
		response.setStatus(HttpServletResponse.SC_OK);
		
	}
	
}
