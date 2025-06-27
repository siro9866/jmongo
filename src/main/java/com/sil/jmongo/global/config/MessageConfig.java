package com.sil.jmongo.global.config;

import com.sil.jmongo.global.util.UtilCommon;
import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


/**
 * message 프로퍼티 사용하기
 * Accept-Language 헤더값 들어오면 자동 변환된다
 * 
 */
@Configuration
public class MessageConfig{

	@Bean
	public LocaleResolver localeResolver() {
		CustomLocaleResolver customLocaleResolver = new CustomLocaleResolver();
		customLocaleResolver.setDefaultLocale(Locale.KOREA);
		return customLocaleResolver;
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		lci.setParamName("lang");
		return lci;
	}

	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasenames("messages/messages", "messages/validation");
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setCacheSeconds(60);
		messageSource.setUseCodeAsDefaultMessage(true);
		return messageSource;
	}

	public static class CustomLocaleResolver extends AcceptHeaderLocaleResolver {
		String[] mLanguageCode = new String[]{"ko", "en"};
		List<Locale> mLocales = Arrays.asList(Locale.forLanguageTag("en"), Locale.forLanguageTag("es"), Locale.forLanguageTag("ko"));

		@NotNull
		@Override
		public Locale resolveLocale(HttpServletRequest request) {
			// 언어팩 변경
			String acceptLanguage = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
			if (UtilCommon.isEmpty(acceptLanguage)) {
				return Locale.getDefault();
			}
			List<Locale.LanguageRange> list = Locale.LanguageRange.parse(request.getHeader("Accept-Language"));
			mLocales = new ArrayList<>();
			for (String code : mLanguageCode) {
				mLocales.add(Locale.forLanguageTag(code));
			}
			return Locale.lookup(list, mLocales);
		}
	}

}
