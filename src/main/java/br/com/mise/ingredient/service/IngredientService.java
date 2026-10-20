package br.com.mise.ingredient.service;

import br.com.mise.ingredient.dto.CreateIngredientRequest;
import br.com.mise.ingredient.entity.Ingredient;
import br.com.mise.ingredient.resource.IngredientRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Locale;

@ApplicationScoped
public class IngredientService {

    @Inject
    IngredientRepository repository;

    public Ingredient create(CreateIngredientRequest request) {

        String name = request.name().trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Ingredient name cannot be blank");
        }

        if (request.unit() == null) {
            throw new IllegalArgumentException("Ingredient unit cannot be null");
        }

        String normalizedName = name.toLowerCase(Locale.ROOT);

        boolean alreadyExists = repository.count(
                "lower(name) = ?1",
                normalizedName) > 0;

        if (alreadyExists) {
            throw new IllegalArgumentException("Ingredient already exists");
        }

        Ingredient ingredient = new Ingredient(
                name,
                request.unit()
        );

        repository.persist(ingredient);
        return ingredient;

    }

}
