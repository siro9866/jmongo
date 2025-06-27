package com.sil.jmongo.global.exception;

import com.sil.jmongo.global.response.ResponseCode;
import lombok.Getter;

/**
 * 커스텀 익셉션
 * 익셉션 강제발생 시킬 경우 주로 사용한다
 */
@Getter
public class CustomException extends RuntimeException  {

	private final ResponseCode responseCode;

	public CustomException(ResponseCode responseCode, String message) {
		super(message);
		this.responseCode = responseCode;
	}
}
