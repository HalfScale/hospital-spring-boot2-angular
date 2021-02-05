package com.springboot.hospital.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.springboot.hospital.validator.FieldsValueMatchValidator;

@Constraint(validatedBy = FieldsValueMatchValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FieldsValueMatch {
	
	String message() default "Fields value don't match";
	
	String field();
	
	String fieldMatch();
	
	public Class<?>[] groups() default {};
	
	public Class<? extends Payload>[] payload() default{};
	
	@Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        FieldsValueMatch[] value();
    }
	
}
