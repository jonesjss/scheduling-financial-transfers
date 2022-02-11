package com.cvc.financial.domain.exception;

public class ExistingEntityException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ExistingEntityException() {
        this("An entity with these parameters already exists");
    }

    public ExistingEntityException(String message) {
        super(message);
    }

}
