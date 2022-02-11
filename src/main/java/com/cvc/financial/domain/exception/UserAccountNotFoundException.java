package com.cvc.financial.domain.exception;

public class UserAccountNotFoundException extends EntityNotFoundException {

	private static final long serialVersionUID = 1L;

	public UserAccountNotFoundException(String message) {
		super(message);
	}

	public UserAccountNotFoundException(Long id, Long userId) {
		this(String.format("User not found with id %d and Account with id %d", userId, id));
	}
	
}