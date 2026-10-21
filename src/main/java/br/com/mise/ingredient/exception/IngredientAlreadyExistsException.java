package br.com.mise.ingredient.exception;

public class IngredientAlreadyExistsException
        extends RuntimeException {

    public IngredientAlreadyExistsException(String name) {
        super("Ingredient already exists: " + name);
    }
}
