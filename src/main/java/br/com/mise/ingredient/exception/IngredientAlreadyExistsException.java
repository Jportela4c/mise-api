package br.com.mise.ingredient.exception;

import br.com.mise.common.exception.BusinessException;
import jakarta.ws.rs.core.Response;

public class IngredientAlreadyExistsException
        extends BusinessException {

    public IngredientAlreadyExistsException(String name) {
        super( "INGREDIENT_ALREADY_EXISTS",
               "Ingredient already exists: " + name,
               Response.Status.CONFLICT);
    }
}
