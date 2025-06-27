package com.sil.jmongo.global.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 실패 응답구조
 * @param <T>
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponseFail<T> {

	private String errorCode;
	private String message;
	private T errors;

	public static <T> ApiResponseFail<T> fail(ResponseCode responseCode, String message) {
		return new ApiResponseFail<>(responseCode, message, null);
	}

	public static <T> ApiResponseFail<T> fail(ResponseCode responseCode, String message, T errors) {
		return new ApiResponseFail<>(responseCode, message, errors);
	}

	private ApiResponseFail(ResponseCode responseCode, String message, T errors) {
		this.errorCode = responseCode.code();
		this.message = message;
		this.errors = errors;
	}
}
