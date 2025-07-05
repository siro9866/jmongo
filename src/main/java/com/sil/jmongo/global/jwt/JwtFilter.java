package com.sil.jmongo.global.jwt;

import com.sil.jmongo.domain.user.dto.UserDto;
import com.sil.jmongo.domain.user.entity.Users;
import com.sil.jmongo.global.exception.GlobalExceptionHandler;
import com.sil.jmongo.global.response.ResponseCode;
import com.sil.jmongo.global.security.CustomUserDetails;
import com.sil.jmongo.global.util.UtilMessage;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtFilter extends OncePerRequestFilter{

	private final JwtUtil jwtUtil;
	private final UtilMessage utilMessage;
	
	public JwtFilter(JwtUtil jwtUtil, UtilMessage utilMessage) {
		this.jwtUtil = jwtUtil;
		this.utilMessage = utilMessage;
	}
	
	/**
	 * 토큰 검증
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
		
		// 헤더에서 access키에 담긴 토큰을 꺼냄
		String accessToken = request.getHeader("accessToken");
		
		// 토큰이 없다면 다음 필터로 처리를 넘긴다
		if(accessToken == null) {
			filterChain.doFilter(request, response);
			return;
		}
		log.debug("accessToken:{}", accessToken);

		// Bearer 제거
		if(accessToken.startsWith("Bearer ")) {
			accessToken = accessToken.substring(7);
		}
		
		// 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
		try {
			// 만료가 되었으면 ExpiredJwtException 발생함
			jwtUtil.isExpired(accessToken);
		} catch (ExpiredJwtException e) {
			// 만료시 다음 필터로 넘기지 않고 응답을 내려줌
			GlobalExceptionHandler.filterExceptionHandler(response, HttpStatus.BAD_REQUEST, ResponseCode.JWT_ACCESSTOKEN_EXPIRED, utilMessage.getMessage("jwt.accessToken.expired", null));
			return;
		} catch (MalformedJwtException e) {
			GlobalExceptionHandler.filterExceptionHandler(response, HttpStatus.BAD_REQUEST, ResponseCode.JWT_ACCESSTOKEN_EXPIRED, utilMessage.getMessage("jwt.accessToken.malformed", null));
			return;
		} catch (Exception e) {
//			e.printStackTrace();
			GlobalExceptionHandler.filterExceptionHandler(response, HttpStatus.BAD_REQUEST, ResponseCode.JWT_ACCESSTOKEN_EXPIRED, utilMessage.getMessage("jwt.accessToken.invalid", null));
			return;
		}
		
		// 토큰이 access 인지 refresh인지 확인(발급시 페이로드에 명시해뒀음)
		String category = jwtUtil.getTokenCategory(accessToken);
		if(!category.equals("accessToken")) {
			// 잘못된 토큰일 경우 다음 필터로 넘기지 않고 응답을 내려줌
			// response status code
			GlobalExceptionHandler.filterExceptionHandler(response, HttpStatus.BAD_REQUEST, ResponseCode.JWT_TOKEN_CATEGORY, utilMessage.getMessage("jwt.accessToken.malformed", null));
			return;
		}
		
		// 토큰을 기반으로 일시적인 세션을 만들어서 토큰내용을 저장
		String username = jwtUtil.getUsername(accessToken);
		String role = jwtUtil.getRole(accessToken);
		
		// MemberEntity를 생성하고 값을 set
		UserDto.LoginResponse loginResponseDto = new UserDto.LoginResponse();
		loginResponseDto.setUsername(username);
		loginResponseDto.setRole(role);
		Users entity = loginResponseDto.toEntity();
		
		// UserDetails에 회원 정보 객체 담기
		CustomUserDetails customUserDetails = new CustomUserDetails(entity);
		
		// 스프링 시큐리티 인증 토큰 생성
		Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
		
		// 세션에 사용자 등록
		SecurityContextHolder.getContext().setAuthentication(authToken);
		
		filterChain.doFilter(request, response);
		
	}

}
