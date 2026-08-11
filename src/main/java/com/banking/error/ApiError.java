package com.banking.error;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

public class ApiError {

	private LocalDateTime timeStamp;
	
	private String error;
	
	private HttpStatus statusCode;
	
	public ApiError() {
		this.setTimeStamp(LocalDateTime.now());
	}
	
	public ApiError(String error, HttpStatus statusCode) {
		this();
		this.setError(error);
		this.setStatusCode(statusCode);
	}

	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(LocalDateTime timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public HttpStatus getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(HttpStatus status) {
		this.statusCode = status;
	}

	
}
