package com.sil.jmongo.global.component;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * application 파일 읽을수 잇게
 */
@Component
public class ApplicationContextServe implements ApplicationContextAware {
	@Getter
    private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(@NotNull ApplicationContext context) throws BeansException {
		applicationContext = context;
	}
}
