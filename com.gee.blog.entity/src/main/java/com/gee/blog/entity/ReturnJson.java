package com.gee.blog.entity;

public class ReturnJson {
	
	private Object result;
	
	private int states;
	
	private String message;

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getStates() {
		return states;
	}

	public void setStates(int states) {
		this.states = states;
	}
	
}
