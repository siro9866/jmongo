package com.sil.jmongo.global.util;

import ch.qos.logback.core.util.StringUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

@Slf4j
public class UtilCommon {

	public static boolean isEmpty(Object obj) {
		if (obj instanceof String)
			return obj == null || "".equals(obj.toString().trim());
		else if (obj instanceof List)
			return obj == null || ((List<?>) obj).isEmpty();
		else if (obj instanceof Map)
			return obj == null || ((Map<?, ?>) obj).isEmpty();
		else if (obj instanceof Object[])
			return obj == null || Array.getLength(obj) == 0;
		else
			return obj == null;
	}

	public static boolean isNotEmpty(Object s) {
		return !isEmpty(s);
	}

	/**
	 * 안전한인트만들기
	 * @param str
	 * @return
	 */
	public static Integer safeParseInt(String str) {
		if (str == null || str.trim().isEmpty()) return null;
		return Integer.valueOf(str.trim());
	}

	/**
	 * 쿠키생성
	 * @param key
	 * @param value
	 * @return
	 */
	public static Cookie createCookie(String key, String value) {
		Cookie cookie = new Cookie(key, value);
		cookie.setMaxAge(24*60*60);	// 생명주기
//		cookie.setSecure(true);	// https 사용할 경우
		cookie.setPath("/");	// 쿠키가 적용될 범위
		cookie.setHttpOnly(true);	// 클라이언트에서 자바스크립트로 쿠키에 접근 할수 없게 하는것
		return cookie;
	}
	
	/**
	 * IP주소가져오기
	 * @param request
	 * @return
	 */
	public static String getRemoteAddr(HttpServletRequest request) {
		List<String> ipHeaders = List.of(
			"X-Forwarded-For",						// 클라이언트의 원 IP 주소를 나타내기 위한 일반적인 헤더, 여러 IP 주소가 쉼표로 구분되어 있을 수 있음
			"HTTP_FORWARDED",				// RFC 7239에 정의된 표준화된 포워드 헤더, 클라이언트 및 프록시 서버 정보를 포함
			"Proxy-Client-IP",							// 일부 프록시 서버에서 사용하는 헤더, 클라이언트 IP 주소를 포함
			"WL-Proxy-Client-IP",					// WebLogic 서버에서 사용하는 헤더, 클라이언트 IP 주소를 포함
			"HTTP_CLIENT_IP",						// HTTP 요청의 클라이언트 IP를 나타내는 헤더, 일부 프록시 서버에서 사용
			"HTTP_X_FORWARDED_FOR",// 클라이언트의 원 IP 주소를 나타내는 또 다른 헤더, X-Forwarded-For와 유사
			"X-RealIP",										// Nginx와 같은 일부 웹 서버에서 사용하는 헤더, 클라이언트의 원 IP 주소를 포함
			"X-Real-IP",									// Nginx와 같은 일부 웹 서버에서 사용하는 헤더, 클라이언트의 원 IP 주소를 포함 (대시 포함 버전)
			"REMOTE_ADDR"						// Java의 ServletRequest에서 제공하는 메서드로, 직접 연결된 클라이언트의 IP 주소를 반환
		);
		
		String clientIp = "";
		// IP_HEADERS 목록에서 클라이언트 IP 확인
		for (String ipHeader : ipHeaders) {
			clientIp = request.getHeader(ipHeader);
//			log.info("헤더 {}: {}", ipHeader, clientIp);
			if (StringUtil.notNullNorEmpty(clientIp) && !"unknown".equalsIgnoreCase(clientIp)) {
//				log.info("헤더있 {}: {}", ipHeader, clientIp);
				return clientIp;
			}
		}
		
		// 모든 헤더에서 IP를 찾지 못한 경우, 기본적으로 제공되는 원격 주소 반환
		clientIp = request.getRemoteAddr();
		log.debug("리모트 주소: " + clientIp);
		return clientIp;
	}
	
}
