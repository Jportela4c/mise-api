
package br.com.mise.ingredient.exception;

import br.com.mise.ingredient.dto.ErrorResponse;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class IngredientAlreadyExistsExceptionMapper
        implements ExceptionMapper<IngredientAlreadyExistsException> {

    @Override
    public Response toResponse(IngredientAlreadyExistsException exception) {

        ErrorResponse error = new ErrorResponse(
                "INGREDIENT_ALREADY_EXISTS",
                exception.getMessage()
        );

        return Response
                .status(Response.Status.CONFLICT)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}
