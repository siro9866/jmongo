package com.sil.jmongo.global.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class UtilDate {

	/**
	 * 당일의시작
	 * @param localDate
	 * @return
	 */
	public static LocalDateTime startDatetime(LocalDate localDate) {
		if(UtilCommon.isEmpty(localDate)) {
			return null;
		}
		return LocalDateTime.of(localDate, LocalTime.of(0,0,0));
	}
	
	/**
	 * 당일의끝
	 * @param localDate
	 * @return
	 */
	public static LocalDateTime endDatetime(LocalDate localDate) {
		if(UtilCommon.isEmpty(localDate)) {
			return null;
		}
		return LocalDateTime.of(localDate, LocalTime.of(23,59,59,999999999));
	}
	
}
