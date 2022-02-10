package com.cvc.financial.domain.exception;

public class UserNotFoundException extends EntityNotFoundException {

	private static final long serialVersionUID = 1L;

	public UserNotFoundException(String message) {
		super(message);
	}
	
	public UserNotFoundException(Long id) {
		this(String.format("User not found with id %d", id));
	}
	
}