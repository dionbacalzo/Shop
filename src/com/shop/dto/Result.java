package com.shop.dto;

public class Result {
	
	public Result () { }
	
	public Result(String status, String message) {
		this.status = status;
		this.message = message;
	}

	private String status;
	
	private String message;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
