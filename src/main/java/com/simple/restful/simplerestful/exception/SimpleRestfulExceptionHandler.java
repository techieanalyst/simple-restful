package com.simple.restful.simplerestful.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import java.time.format.DateTimeParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.simple.restful.simplerestful.api.ApiError;

@ControllerAdvice
public class SimpleRestfulExceptionHandler {

	private static Logger logger = LoggerFactory.getLogger(SimpleRestfulExceptionHandler.class);
	
	@ResponseStatus(BAD_REQUEST)
	@ExceptionHandler({ MethodArgumentTypeMismatchException.class })
	@ResponseBody
	public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
	  MethodArgumentTypeMismatchException ex, WebRequest request) {
	    String error = 
	      ex.getName() + " should be of type " + ex.getRequiredType().getName();
	 
	    logger.error("Error occurred due to invalid arguments", ex);
	    ApiError apiError = 
	      new ApiError(BAD_REQUEST, ex.getLocalizedMessage(), error);
	    return ResponseEntity.badRequest().body(apiError);
	}
	
	@ResponseStatus(INTERNAL_SERVER_ERROR)
	@ExceptionHandler({ DateTimeParseException.class })
	@ResponseBody
	public ResponseEntity<Object> handleDateTimeParseException(DateTimeParseException ex, WebRequest request) {
	    String error = "Cannot parse " + ex.getParsedString();
	    
	    logger.error("Error occurred due to parsing error", ex);
	    ApiError apiError = 
	      new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), error);
	    return ResponseEntity.badRequest().body(apiError);
	}
	
	@ResponseStatus(INTERNAL_SERVER_ERROR)
	@ExceptionHandler({ ArrayIndexOutOfBoundsException.class })
	@ResponseBody
	public ResponseEntity<Object> handleIndexOutOfBoundsExceptions(IndexOutOfBoundsException ex, WebRequest request) {
		String error = null;
		if (ex instanceof ArrayIndexOutOfBoundsException) {
			error = "A process encountered an index that's not found within the array"; 
		}
		
		logger.error("Error occurred due to index out of bounds error", ex);
	    ApiError apiError = 
	      new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), error);
	    return ResponseEntity.badRequest().body(apiError);
	}

}
