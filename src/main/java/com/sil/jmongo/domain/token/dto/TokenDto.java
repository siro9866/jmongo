package com.sil.jmongo.domain.token.dto;

import com.sil.jmongo.domain.token.entity.Tokens;
import lombok.*;

import java.time.LocalDateTime;


public class TokenDto {

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@ToString
	public static class CreateRequest {
		private String username;
		private String refreshToken;
		private LocalDateTime refreshTokenExpiration;

		// DTO -> Entity 로 변환
		public Tokens toEntity() {
			return Tokens.builder()
					.username(username)
					.refreshToken(refreshToken)
					.refreshTokenExpiration(refreshTokenExpiration)
					.build();
		}
	}

}
