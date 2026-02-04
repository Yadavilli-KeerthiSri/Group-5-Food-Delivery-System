package com.cg.exception;

public class ResourceNotFoundException extends RuntimeException{

	public ResourceNotFoundException(String messages) {
		super(messages);
	}
	
}