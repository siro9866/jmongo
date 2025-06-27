package com.sil.jmongo.global.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sil.jmongo.global.response.ApiResponseFail;
import com.sil.jmongo.global.response.ResponseCode;
import com.sil.jmongo.global.util.UtilMessage;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 전역 예외 핸들러
 */
@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {
	
	private final UtilMessage utilMessage;
	
	/**
	 * 글로벌 익셉션
	 * 일반적인 익셉션은 여기로 다 들어온다
	 */
	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<ApiResponseFail<?>> exceptionHandler(Exception e) {
		log.debug("GlobalExceptionHandler:Exception");
		e.printStackTrace();
//		log.error(e.getMessage());
//		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseFail.fail(ResponseCode.INTERNAL_SERVER_ERROR, ResponseCode.INTERNAL_SERVER_ERROR.message()));
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseFail.fail(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage()));
	}

	/**
	 * 데이타베이스 익셉션
	 */
	@ExceptionHandler(value = DataAccessException.class)
	public ResponseEntity<ApiResponseFail<?>> exceptionHandler(DataAccessException e) {
		log.debug("GlobalExceptionHandler:DataAccessException");
		log.error(e.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseFail.fail(ResponseCode.DATA_ACCESS_EXCEPTION, ResponseCode.DATA_ACCESS_EXCEPTION.message()));
	}
	
	/**
	 * 쿼리 익셉션
	 */
	@ExceptionHandler(value = SQLException.class)
	public ResponseEntity<ApiResponseFail<?>> exceptionHandler(SQLException e) {
		log.debug("GlobalExceptionHandler:SQLException");
		log.error(e.getMessage());
		for(StackTraceElement error : e.getStackTrace()) {log.debug(error.toString());}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseFail.fail(ResponseCode.SQL_EXCEPTION, ResponseCode.SQL_EXCEPTION.message()));
	}
	
	/**
	 * 지원하지 않은 HTTP method 호출 할 경우 발생
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class) // HttpRequestMethodNotSupportedException 예외를 잡아서 처리
	protected ResponseEntity<ApiResponseFail<?>> exceptionHandler(HttpRequestMethodNotSupportedException e) {
		log.debug("GlobalExceptionHandler:HttpRequestMethodNotSupportedException");
		log.error(e.getMessage());
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(ApiResponseFail.fail(ResponseCode.METHOD_NOT_SUPPORTED_EXCEPTION, ResponseCode.METHOD_NOT_SUPPORTED_EXCEPTION.message()));
	}
	
	/**
	 * 404 not found
	 */
	@ExceptionHandler(NoHandlerFoundException.class)
	protected ResponseEntity<ApiResponseFail<?>> exceptionHandler(NoHandlerFoundException e) {
		log.debug("GlobalExceptionHandler:NoHandlerFoundException");
		log.error(e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseFail.fail(ResponseCode.NO_HANDLER_FOUND_EXCEPTION, ResponseCode.NO_HANDLER_FOUND_EXCEPTION.message()));
	}
	
	/**
	 * 커스텀 익셉션
	 * 의도한 익셉션의 경우 여기로 들어온다
	 * throw new CustomException(ExceptionClass.PROVIDER, HttpStatus.FORBIDDEN, "접근금지");
	 */
	@ExceptionHandler(value = CustomException.class)
	public ResponseEntity<ApiResponseFail<?>> exceptionHandler(CustomException e) {
		log.debug("GlobalExceptionHandler:CustomException");
		log.debug("e.getMessage() = {}", e.getMessage());
		log.debug("responseCode.code = {}", e.getResponseCode().code());
		log.debug("responseCode.message = {}", e.getResponseCode().message());
		for(StackTraceElement error : e.getStackTrace()) {log.debug(error.toString());}
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponseFail.fail(e.getResponseCode(), e.getMessage()));
	}
	
	/**
	 * validation 익셉션 발생시
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponseFail<?>> exceptionHandler(final MethodArgumentNotValidException e) {
		log.debug("GlobalExceptionHandler:MethodArgumentNotValidException");
		log.debug(e.getMessage());
		// @valid 어토테이션과 dto의 제약으로 발생된 오류
		List<ErrorDto> errors = new ArrayList<>();
		e.getBindingResult().getAllErrors().forEach(error -> errors.add(new ErrorDto(((FieldError) error).getField(), error.getDefaultMessage())));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseFail.fail(ResponseCode.METHOD_ARGUMENT_NOT_VALID_EXCEPTION, utilMessage.getMessage("exception.valid.anotation", null), errors));
	}
	
	// 필터등에서 exception이 발생할 경우 advice 범위 밖이라 여기로 안들어옴. 데이타 가공 
	public static void filterExceptionHandler(HttpServletResponse response, HttpStatus httpStatus, ResponseCode responseCode, String message) {
		log.debug("GlobalExceptionHandler:filterExceptionHandler");
		response.setStatus(httpStatus.value());
		response.setContentType("application/json;charset=UTF-8");
		try {
			Map<String, Object> responseBody = new HashMap<>();
			responseBody.put("errorCode", responseCode.code());
			responseBody.put("message", message);
			responseBody.put("errors", null);
			PrintWriter writer = response.getWriter();
			writer.write(new ObjectMapper().writeValueAsString(responseBody));
			writer.flush();
			writer.close();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	@Getter
	@NoArgsConstructor
	static class ErrorDto{
		private String errorField;
		private String errorMessage;

		public ErrorDto(String errorField, String errorMessage) {
			this.errorField = errorField;
			this.errorMessage = errorMessage;
		}
	}
}