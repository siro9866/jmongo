package com.sil.jmongo.global.validation;

/**
 * Enum 으로 저의된 항목 파라미터 validation
 * FileName    : IntelliJ IDEA
 * Author      : Seowon
 * Date        : 2025-02-26
 * Description :
 */
import com.sil.jmongo.global.util.UtilMessage;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.stream.Collectors;

public class EnumValidator implements ConstraintValidator<EnumValid, String> {
    @Autowired
    private UtilMessage utilMessage;
    private String allowedValues;

    @Override
    public void initialize(EnumValid constraintAnnotation) {
        Class<? extends Enum<?>> enumClass = constraintAnnotation.enumClass();
        // 허용된 Enum 값을 문자열로 변환 (예: "ACTIVE, INACTIVE, PENDING")
        allowedValues = Arrays.stream(enumClass.getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // null 허용 (필수값 체크는 다른 어노테이션에서 처리)
        }

        boolean isValid = Arrays.stream(allowedValues.split(", "))
                .anyMatch(enumValue -> enumValue.equals(value));

        if (!isValid) {
            StringBuffer sb = new StringBuffer();
            sb.append("[");
            sb.append(value);
            sb.append("]");
            sb.append(" ");
            sb.append(utilMessage.getMessage("validation.invalid.value", null));
            sb.append(": ");
            sb.append(allowedValues);
            System.out.println("context = " + context.getDefaultConstraintMessageTemplate());

            // 기본 메시지를 동적으로 변경
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(sb.toString()).addConstraintViolation();
        }

        return isValid;
    }
}
