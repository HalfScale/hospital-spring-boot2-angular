package com.springboot.hospital.handler;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springboot.hospital.entity.Response;
import com.springboot.hospital.restcontroller.UserRestController;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	Logger logger = LoggerFactory.getLogger(UserRestController.class);

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	public ResponseEntity<Object> customValidationErrorHandling(MethodArgumentNotValidException ex) {
		logger.info("total errors: " + ex.getAllErrors().size());
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.contentType(MediaType.APPLICATION_JSON)
				.body(new Response(1, "Validation error", errors));
	}
	
//	@ExceptionHandler(Exception.class)
//    @ResponseBody
//    public ResponseEntity<Object> handleAllOtherErrors(Exception exception) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(new Response(1, exception.getMessage(), null));
//    }
}
