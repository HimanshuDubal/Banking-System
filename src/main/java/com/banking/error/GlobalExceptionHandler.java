package com.banking.error;

import java.nio.file.AccessDeniedException;

import javax.naming.AuthenticationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.JwtException;


@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<ApiError> handlerUsernameNotFoundException(UsernameNotFoundException ex){
		ApiError apiError = new ApiError("Username is not found with this Username " + ex.getMessage(), HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(apiError,apiError.getStatusCode());
	}
	
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ApiError> handlerAuthenticationException(AuthenticationException ex){
		ApiError apiError = new ApiError("Authentication Failed " + ex.getMessage(),HttpStatus.UNAUTHORIZED);
		return new ResponseEntity<>(apiError,apiError.getStatusCode());

	}
	
	@ExceptionHandler(JwtException.class)
	public ResponseEntity<ApiError> handlerJwtException(JwtException ex){
		ApiError apiError = new ApiError("Invalid JWT Token " + ex.getMessage(), HttpStatus.UNAUTHORIZED);
		return new ResponseEntity<>(apiError,apiError.getStatusCode());
	}
	
	@ExceptionHandler(AccessDeniedException.class)
		public ResponseEntity<ApiError> handlerAccessDeniedException(AccessDeniedException ex){
			ApiError apiError = new ApiError("Access Denied Insufficient Permission ", HttpStatus.FORBIDDEN);
			return new ResponseEntity<>(apiError,apiError.getStatusCode());
		
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handlerException(Exception ex){
		ApiError apiError = new ApiError("An Unexpected Error Occured " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(apiError,apiError.getStatusCode());
	}
}
