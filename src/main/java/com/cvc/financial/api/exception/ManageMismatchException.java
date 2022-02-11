package com.cvc.financial.api.exception;

import org.springframework.http.HttpStatus;

public class ManageMismatchException {

    public Problem.ProblemBuilder manage(HttpStatus status, Throwable throwable) {
        MismatchedInputHandler invalidFormat = new MismatchedInputHandler.HandleInvalidFormatException();
        MismatchedInputHandler unrecognizedProperty = new MismatchedInputHandler.HandleUnrecognizedPropertyException();
        MismatchedInputHandler ignoredProperty = new MismatchedInputHandler.HandleIgnoredPropertyException();
        MismatchedInputHandler mismatchedInput = new MismatchedInputHandler.HandleMismatchedInputException();

        invalidFormat.setNext(unrecognizedProperty);
        unrecognizedProperty.setNext(ignoredProperty);
        ignoredProperty.setNext(mismatchedInput);

        return invalidFormat.createProblemBuilder(status, throwable);
    }
}