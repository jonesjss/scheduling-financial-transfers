package com.cvc.financial.api.exception;

import com.fasterxml.jackson.databind.exc.IgnoredPropertyException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;
import java.util.stream.Collectors;

public abstract class MismatchedInputHandler {
    private MismatchedInputHandler next;
    private Class<? extends MismatchedInputException> type;

    protected MismatchedInputHandler(Class<? extends MismatchedInputException> type) {
        this.type = type;
    }
    public void setNext(MismatchedInputHandler next) {
        this.next = next;
    }

    abstract String detail(MismatchedInputException mie, String path);

    public Problem.ProblemBuilder createProblemBuilder(HttpStatus status,
                                                       Throwable throwable) {
        if(!this.isInstance(throwable)) {
            return this.next.createProblemBuilder(status, throwable);
        }

        MismatchedInputException mie = (MismatchedInputException) throwable;
        String attributeError = this.getAttribute(mie);
        String detail = this.detail(mie, attributeError);
        ProblemType problemType = ProblemType.INCOMPREHESIBLE_MESSAGE;

        return Problem.builder()
                .type(problemType.getUri())
                .title(problemType.getTitle())
                .status(status.value())
                .timestamp(OffsetDateTime.now())
                .detail(detail);
    }

    protected String getAttribute(MismatchedInputException ex) {
        return ex.getPath()
                .stream().map(ref -> ref.getFieldName()).collect(Collectors.joining("."));
    }

    public boolean isInstance(Throwable throwable) {
        return this.type.isInstance(throwable);
    }

    public static class HandleInvalidFormatException
            extends MismatchedInputHandler {

        public HandleInvalidFormatException() {
            super(InvalidFormatException.class);
        }

        @Override
        public String detail(MismatchedInputException mie, String path) {
            InvalidFormatException ex = (InvalidFormatException) mie;

            return String.format("Property '%s' received value '%s', which is of invalid type. " +
                            "Correct and enter the value compatible with type '%s.'", path, ex.getValue(),
                    ex.getTargetType().getSimpleName());
        }
    }

    public static class HandleUnrecognizedPropertyException
            extends MismatchedInputHandler {

        public HandleUnrecognizedPropertyException() {
            super(UnrecognizedPropertyException.class);
        }

        @Override
        public String detail(MismatchedInputException mie, String path) {
            return String.format("Property '%s' is not recognized", path);
        }
    }

    public static class HandleIgnoredPropertyException
            extends MismatchedInputHandler {

        protected HandleIgnoredPropertyException() {
            super(IgnoredPropertyException.class);
        }

        @Override
        String detail(MismatchedInputException mie, String path) {
            return String.format("Property '%s' is being ignored for this operation.", path);
        }
    }

    public static class HandleMismatchedInputException extends
            MismatchedInputHandler {

        public HandleMismatchedInputException() {
            super(MismatchedInputException.class);
        }

        @Override
        public boolean isInstance(Throwable throwable) {
            return true;
        }

        @Override
        String detail(MismatchedInputException mie, String path) {
            return mie.getMessage();
        }
    }
}