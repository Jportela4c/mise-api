
package br.com.mise.common.exception;

import br.com.mise.common.dto.ErrorResponse;
import io.quarkus.hibernate.validator.runtime.jaxrs.ResteasyReactiveViolationException;
import jakarta.validation.ConstraintViolation;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.stream.Collectors;

@Provider
public class ValidationExceptionMapper
        implements ExceptionMapper<ResteasyReactiveViolationException> {

    @Override
    public Response toResponse(
            ResteasyReactiveViolationException exception) {

        String message = exception.getConstraintViolations()
                .stream()
                .map(this::formatViolation)
                .sorted()
                .collect(Collectors.joining("; "));

        ErrorResponse error = new ErrorResponse(
                "VALIDATION_ERROR",
                message
        );

        return Response
                .status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }

    private String formatViolation(ConstraintViolation<?> violation) {
        String path = violation.getPropertyPath().toString();
        String field = path.substring(path.lastIndexOf('.') + 1);

        return field + ": " + violation.getMessage();
    }
}
