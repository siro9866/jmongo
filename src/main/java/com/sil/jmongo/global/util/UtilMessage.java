package com.sil.jmongo.global.util;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * 다국어메세지
 */
@Component
public class UtilMessage {
	private MessageSource messageSource;

	public UtilMessage(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * 언어팩 언어 가져오기
	 * 사용법: utilMessage.getMessage("validation.item");
	 * @param code 언어팩 코드
	 * @return String
	 */
	public String getMessage(String code) {
		return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
	}

	/**
	 * 언어팩 언어 가져오기
	 * 사용법: utilMessage.getMessage("validation.item2", new String[] {"정글", "맑음", "흐림"});
	 * @param code 언어팩 코드
	 * @param strs 동적 문자
	 * @return String
	 */
	public String getMessage(String code, String[] strs) {
		return messageSource.getMessage(code, strs, LocaleContextHolder.getLocale());
	}
	
	/**
	 * 언어팩 언어 가져오기
	 * 사용법: utilMessage.getMessage("validation.item2", new String[] {"정글", "맑음", "흐림"});
	 * @param code 언어팩 코드
	 * @param strs 동적 문자
	 * @param locale 지역
	 * @return message
	 */
	public String getMessage(String code, String[] strs, Locale locale) {
		return messageSource.getMessage(code, strs, locale);
	}
}
