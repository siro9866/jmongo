package com.sil.jmongo.global.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * 등록 수정시 밸리데이션
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {PhoneValidator.class})
@Documented
public @interface Phone {

	String message() default "{validation.phone}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}