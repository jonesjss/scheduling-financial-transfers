package com.cvc.financial.domain.exception;

public class TransferNotFoundException extends EntityNotFoundException {

	private static final long serialVersionUID = 1L;

	public TransferNotFoundException(String message) {
		super(message);
	}
	
}