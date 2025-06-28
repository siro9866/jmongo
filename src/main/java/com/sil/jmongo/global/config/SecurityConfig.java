package com.sil.jmongo.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sil.jmongo.domain.token.repository.TokenRepository;
import com.sil.jmongo.global.jwt.*;
import com.sil.jmongo.global.util.UtilMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final AuthenticationConfiguration authenticationConfiguration;
	private final TokenRepository tokenRepository;
	private final JwtUtil jwtUtil;
	private final UtilMessage utilMessage;
	private final ObjectMapper objectMapper;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

	@Value("${custom.server.host.front}") String HOST_FRONT;

	/**
	 * AuthenticationManager 를 반환
	 */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
		return configuration.getAuthenticationManager();
	}

	/**
	 * 비밀번호암호화
	 */
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		
		// cors 설정
		http
			.cors((cors) -> cors
					.configurationSource(request -> {
                        CorsConfiguration configuration = new CorsConfiguration();
                        configuration.setAllowedOrigins(Collections.singletonList(HOST_FRONT));	// 프런트와 백의 url이 달라서 허용할 포트
                        configuration.setAllowedMethods(Collections.singletonList("*"));		// 허용할 메서드
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));		// 허용할 헤더
                        configuration.setMaxAge(3600L);											// 허용할 시간
                        configuration.setExposedHeaders(Collections.singletonList("accessToken"));	// 클라이언트로 헤더 보내줄때 이 헤더도 허용
                        return configuration;
                    }));
		
		// csrf 사용안함
		http
			.csrf(AbstractHttpConfigurer::disable);
		
		// form 로그인방식 사용안함
		http
			.formLogin(AbstractHttpConfigurer::disable);
		
		// http basic 인증방식 사용안함
		http
			.httpBasic(AbstractHttpConfigurer::disable);

		// url 접근 허용
		http
			.authorizeHttpRequests((auth) -> auth
					.requestMatchers(
							"/join"
							, "/login"
							, "/reIssue"

							/* swagger */
							, "/swagger-ui/**"
							, "/api/swagger-config"

							/* 기타 예외 */
							, "/api/**"
							, "/global/**"
					).permitAll()
//					.requestMatchers("/api/admin").hasRole("ADMIN")
//					.requestMatchers("/api/user/**").hasRole("USER")
					.anyRequest().authenticated());
		
		// JWT 관련 필터 설정 및 예외 처리
		http
			.exceptionHandling((exceptionHandling) -> exceptionHandling
				.accessDeniedHandler(jwtAccessDeniedHandler)
				.authenticationEntryPoint(jwtAuthenticationEntryPoint));
		
		// 필터등록
		http
			.addFilterBefore(new JwtFilter(jwtUtil, utilMessage), LoginFilter.class);
		http
			.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, tokenRepository, objectMapper, utilMessage), UsernamePasswordAuthenticationFilter.class);	// addFilterAt 는 기존 필터의 위치에 대체하겠다는 거임
		http
			.addFilterBefore(new LogoutFilterCustom(jwtUtil, tokenRepository), LogoutFilter.class);
		
		// 세션설정(jwt 방식)
		http
			.sessionManagement((session) -> session
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		
		return http.build();
	}
}
