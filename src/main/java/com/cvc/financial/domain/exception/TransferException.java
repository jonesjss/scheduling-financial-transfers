package com.cvc.financial.domain.exception;

public class TransferException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public TransferException(String message) {
        super(message);
    }
}
