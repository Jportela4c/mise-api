package br.com.mise.ingredient.exception;

import br.com.mise.common.exception.BusinessException;
import jakarta.ws.rs.core.Response;

public class IngredientNotFoundException extends BusinessException {

    public IngredientNotFoundException(Long id) {
        super("INGREDIENT_NOT_FOUND",
              "Ingredient not found: " + id,
              Response.Status.NOT_FOUND);
    }
}
