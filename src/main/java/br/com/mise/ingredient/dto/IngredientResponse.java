
package br.com.mise.ingredient.dto;

import br.com.mise.ingredient.entity.Ingredient;
import br.com.mise.ingredient.entity.MeasurementUnit;

public record IngredientResponse(
        Long id,
        String name,
        MeasurementUnit unit
) {
    public static IngredientResponse from (Ingredient ingredient) {
        return new IngredientResponse(
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getUnit()
        );
    }
}
