package com.cvc.financial.api.exception;

import com.cvc.financial.domain.exception.EntityNotFoundException;
import com.cvc.financial.domain.exception.ExistingEntityException;
import com.cvc.financial.domain.exception.TransferException;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.internal.util.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    private final MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status,
                                                         WebRequest request) {

        return handleValidationInternal(ex, ex.getBindingResult(), headers, status, request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException ex,
                                                                  WebRequest request) {
        final var problem = this.createProblemBuilder(
                        HttpStatus.NOT_FOUND,
                        ProblemType.RESOURCE_NOT_FOUND,
                        ex.getMessage())
                .build();

        return this.handleExceptionInternal(ex, problem,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(TransferException.class)
    public ResponseEntity<?> handleTransferException(TransferException ex,
                                                                  WebRequest request) {
        final var problem = this.createProblemBuilder(
                        HttpStatus.BAD_REQUEST,
                        ProblemType.TRANSFER_RULE,
                        ex.getMessage())
                .build();

        return this.handleExceptionInternal(ex, problem,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(ExistingEntityException.class)
    public ResponseEntity<?> handleExistingEntityException(ExistingEntityException ex,
                                                                  WebRequest request) {
        final var problem = this.createProblemBuilder(
                        HttpStatus.BAD_REQUEST,
                        ProblemType.EXISTING_ENTITY,
                        ex.getMessage())
                .build();

        return this.handleExceptionInternal(ex, problem,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        final var problem = this.createProblemBuilder(
                HttpStatus.NOT_FOUND,
                ProblemType.RESOURCE_NOT_FOUND,
                String.format("The resource '%s' you tried to access does not exist.", ex.getRequestURL())
        ).build();

        return this.handleExceptionInternal(ex, problem,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Throwable rootCause = ExceptionUtils.getRootCause(ex);

        final var manageMismatch = new ManageMismatchException();

        final var problem = manageMismatch.manage(status, rootCause).build();

        return this.handleExceptionInternal(ex, problem,
                new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        var methodArgument = (MethodArgumentTypeMismatchException) ex;

        final var problem = this.createProblemBuilder(
                        HttpStatus.BAD_REQUEST,
                        ProblemType.INVALID_PARAMETER,
                        String.format("The URL parameter '%s' was given the value '%s', which is of an invalid type. " +
                                        "Correct and enter a value compatible with '%s'.",
                                methodArgument.getName(),
                                methodArgument.getValue(),
                                methodArgument.getRequiredType().getSimpleName()))
                .build();

        return this.handleExceptionInternal(ex, problem,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
        final var problem = this.createProblemBuilder(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ProblemType.SYSTEM_ERROR,
                "An unexpected system error has occurred. Try again and if the" +
                        "problem persists, contact your system administrator."
        ).build();

        return this.handleExceptionInternal(ex, problem,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleValidationInternal(ex, ex.getBindingResult(), headers, status, request);
    }
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
                                                             HttpHeaders headers, HttpStatus status,
                                                             WebRequest request) {
        if(body == null) {
            body = this.createProblemBuilder(status, null, ex.getMessage())
                    .title(status.getReasonPhrase())
                    .build();
        }
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    private ResponseEntity<Object> handleValidationInternal(Exception ex, BindingResult bindingResult, HttpHeaders headers,
                                                            HttpStatus status, WebRequest request) {
        ProblemType problemType = ProblemType.INVALID_DATA;
        String detail = "One or more fields are invalid. Please fill in correctly and try again.";

        List<Problem.Error> problemObjects = bindingResult.getAllErrors().stream()
                .map(objectError -> {
                    String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());

                    String name = objectError.getObjectName();

                    if (objectError instanceof FieldError) {
                        name = ((FieldError) objectError).getField();
                    }

                    return Problem.Error.builder()
                            .name(name)
                            .userMessage(message)
                            .build();
                })
                .collect(Collectors.toList());

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .errors(problemObjects)
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private Problem.ProblemBuilder createProblemBuilder(HttpStatus status,
                                                        ProblemType problemType, String detail) {
        var problem = Problem.builder()
                .status(status.value())
                .timestamp(OffsetDateTime.now())
                .detail(detail);

        if(problemType != null) {
            problem.type(problemType.getUri()).title(problemType.getTitle());
        }
        return problem;
    }
}