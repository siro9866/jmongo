package com.sil.jmongo.global.util;

import com.sil.jmongo.global.component.ApplicationContextServe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

/**
 * 프로퍼티파일 자바로 읽기
 * util 등 빈 주입 안되면 도통 잘 안들어감
 */
@Slf4j
public class UtilProperty {
	public static String getProperty(String propertyName) {
		return getProperty(propertyName, null);
	}

	public static String getProperty(String propertyName, String defaultValue) {
		String value = defaultValue;
		ApplicationContext applicationContext = ApplicationContextServe.getApplicationContext();
		if (applicationContext.getEnvironment().getProperty(propertyName) == null) {
			log.warn(propertyName + " properties was not loaded.");
		} else {
			value = applicationContext.getEnvironment().getProperty(propertyName).toString();
		}
		return value;
	}
}
