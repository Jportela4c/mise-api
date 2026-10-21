package br.com.mise.ingredient.exception;

import common.exception.BusinessException;
import common.exception.BusinessExceptionMapper;
import jakarta.ws.rs.core.Response;

public class IngredientAlreadyExistsException
        extends BusinessException {

    public IngredientAlreadyExistsException(String name) {
        super( "INGREDIENT_ALREADY_EXISTS",
               "Ingredient already exists: " + name,
               Response.Status.CONFLICT);
    }
}
