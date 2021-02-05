package com.springboot.hospital.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

import com.springboot.hospital.annotation.FieldsValueMatch;

@Component
public class FieldsValueMatchValidator implements ConstraintValidator<FieldsValueMatch, Object>{
	
	private String field;
    private String fieldMatch;
    private String errorMessage;

    public void initialize(FieldsValueMatch constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.fieldMatch = constraintAnnotation.fieldMatch();
        this.errorMessage = constraintAnnotation.message();
    }

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		Object fieldValue = new BeanWrapperImpl(value).getPropertyValue(field);
		Object fieldMatchValue = new BeanWrapperImpl(value).getPropertyValue(fieldMatch);
		
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(errorMessage).addPropertyNode(field).addConstraintViolation();
		
		if (!((String) fieldMatchValue).isEmpty()) {
			context.buildConstraintViolationWithTemplate(errorMessage).addPropertyNode(fieldMatch).addConstraintViolation();
		}
		
		
		
		return fieldValue.equals(fieldMatchValue);
	}
	
}
