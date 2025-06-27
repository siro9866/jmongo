package com.sil.jmongo.domain.token.entity;

import com.sil.jmongo.global.entity.Base;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * 리프레시토큰관리
 * 필드 추가등 학인필요
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tokens")
public class Tokens extends Base {
	
	@Id
	private String id;
	private String username;
	private String refreshToken;	// 리프레쉬토큰
	private LocalDateTime refreshTokenExpiration;	// 리프레시토큰만료일시
}
