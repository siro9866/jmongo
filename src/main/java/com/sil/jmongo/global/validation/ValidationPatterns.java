package com.sil.jmongo.global.validation;

/**
 * Validation 정규식 정의
 */
public class ValidationPatterns {

    // 공백 없는 문자열
    public static final String NO_WHITESPACE = "^[^\\s]+$";

    // 이메일 형식에 대한 커스텀 정규표현식이 필요한 경우
    public static final String EMAIL_FORMAT = "^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";

    // 추가로 사용할 패턴들 예시
    public static final String ONLY_KOREAN = "^[가-힣]+$";
    public static final String ONLY_NUMERIC = "^[0-9]+$";

    private ValidationPatterns() {
        // 상수 클래스는 생성자 막기
    }
}
