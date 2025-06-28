package com.sil.jmongo.global.response;

/**
 * 응답코드
 */
public enum ResponseCode {
	INTERNAL_SERVER_ERROR("1001", "서버에러")
	, DATA_ACCESS_EXCEPTION("1002", "데이타베이스 익셉션")
	, SQL_EXCEPTION("1003", "SQL 익셉션")
	, METHOD_NOT_SUPPORTED_EXCEPTION("1004", "지원하지 않은 HTTP method")
	, NO_HANDLER_FOUND_EXCEPTION("1005", "페이지를 찾을 수 없습니다")
	, METHOD_ARGUMENT_NOT_VALID_EXCEPTION("1006", "validation 익셉션")

	, BAD_REQUEST("1099", "잘못된요청")

	, ACCESS_DENIED("1101", "인가 실패")
	, AUTHENTICATION_DENIED("1102", "인증 실패")
	, LOGIN_FAIL("1103", "로그인 실패")
	, JWT_ACCESSTOKEN_EXPIRED("1111", "토큰 만료")
	, JWT_ACCESSTOKEN_MALFORMEDD("1112", "손상된 토큰")
	, JWT_ACCESSTOKEN_INVALID("1113", "유효하지 않은 토큰")
	, JWT_TOKEN_CATEGORY("1114", "토큰 카테고리 확인")

	, JWT_REFRESHTOKEN_EMPTY("1201", "리프레쉬토큰 없음")
	, JWT_REFRESHTOKEN_EXPIRED("1202", "리프레쉬토큰 만료")
	, JWT_REFRESHTOKEN_INVALID("1203", "리프레쉬토큰 유효하지않음")
	, JWT_REFRESHTOKEN_NODATA("1204", "리프레쉬토큰 DB에 없음")
	, EXCEPTION_NODATA("1205", "조회된 데이타없음")

	;

	private final String code;
	private final String message;

	ResponseCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String code() {
		return code;
	}
	public String message() {
		return message;
	}

}
