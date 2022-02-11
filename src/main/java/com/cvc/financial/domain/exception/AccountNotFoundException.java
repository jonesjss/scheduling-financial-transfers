package com.cvc.financial.domain.exception;

public class AccountNotFoundException extends EntityNotFoundException {

	private static final long serialVersionUID = 1L;

	public AccountNotFoundException(String message) {
		super(message);
	}

	public AccountNotFoundException(String agency, String accountNumber) {
		this(String.format("Agency %s and accountNumber %s not found", agency, accountNumber));
	}
	
}