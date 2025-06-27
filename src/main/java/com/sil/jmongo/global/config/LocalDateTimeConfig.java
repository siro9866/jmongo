package com.sil.jmongo.global.config;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;

/**
 * json to java(입력), java to json(출력)
 * 으로 변환할때 LocalDate, LocalDateTime 포멧지정
 */
@Configuration
public class LocalDateTimeConfig{

	@Value("${custom.format.date}") String FORMAT_DATE;
	@Value("${custom.format.dateStr}") String FORMAT_DATESTR;
	@Value("${custom.format.datetime}") String FORMAT_DATETIME;
	@Value("${custom.format.datetimeStr}") String FORMAT_DATETIMESTR;
	
	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
		return builder -> {
			// LocalDate 타입 역직렬화(javaTOjson) 패턴
			DateTimeFormatter localDateSerializeFormatter = DateTimeFormatter.ofPattern(FORMAT_DATESTR);
			builder.serializers(new LocalDateSerializer(localDateSerializeFormatter));

			// LocalDate 타입 직렬화(jsonTOjava) 패턴
			DateTimeFormatter localDateDeserializeFormatter = DateTimeFormatter.ofPattern(FORMAT_DATE);
			builder.deserializers(new LocalDateDeserializer(localDateDeserializeFormatter));

			// LocalDateTime 타입 역직렬화(javaTOjson) 패턴
			DateTimeFormatter localDateTimeSerializeFormatter = DateTimeFormatter.ofPattern(FORMAT_DATETIME);
			builder.serializers(new LocalDateTimeSerializer(localDateTimeSerializeFormatter));
			// LocalDateTime 타입 직렬화(jsonTOjava) 패턴
			DateTimeFormatter localDateTimeDeserializeFormatter = DateTimeFormatter.ofPattern(FORMAT_DATETIMESTR);
			builder.deserializers(new LocalDateTimeDeserializer(localDateTimeDeserializeFormatter));
		};
	}
}
