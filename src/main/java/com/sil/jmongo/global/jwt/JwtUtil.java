package com.sil.jmongo.global.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

	private final SecretKey secretKey;
	
	public JwtUtil(@Value("${spring.jwt.secret}") String secret) {
		this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());	// 객체키 만듬
	}
	
	
	/**
	 * 아이디 가져오기
	 */
	public String getUsername(String token) {
		// 암호화된 토큰을 보안키를 적용해서 확인한다.
		// 특정 데이타를 가져온다
		return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
	}

	/**
	 * Role 을 가져온다
	 */
	public String getRole(String token) {
		// 암호화된 토큰을 보안키를 적용해서 확인한다.
		// 특정 데이타를 가져온다
		return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
	}

	/**
	 * access인지, refresh토큰인지 구분
	 */
	public String getTokenCategory(String token) {
		return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("tokenCategory", String.class);
	}
	
	/**
	 * 토큰 만료 여부
	 */
	public void isExpired(String token) {
		// 암호화된 토큰을 보안키를 적용해서 확인한다.
		// 토큰 만료확인
		Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration();
	}
	
	
	/**
	 * 토큰 발행
	 */
	public String createJwt(String tokenCategory, String username, String role, Long expiredMs) {
		return Jwts.builder()
				.setHeader(Map.of("typ", "JWT"))
				.claim("tokenCategory", tokenCategory)	// access인지, refresh토큰인지 구분값
				.claim("username", username)
				.claim("role", role)
				.issuedAt(new Date(System.currentTimeMillis()))		// 발행일시
				.expiration(new Date(System.currentTimeMillis() + expiredMs))		// 만료일시(발행일시 + 유횩간)
				.signWith(secretKey)		// 보안키를 이용해 암호화
				.compact();
	}
	
	
}
