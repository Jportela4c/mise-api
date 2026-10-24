package br.com.mise.ingredient.dto;

import br.com.mise.ingredient.entity.MeasurementUnit;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateIngredientRequest(
        @NotBlank String name,
        @NotNull MeasurementUnit unit
) {
}
