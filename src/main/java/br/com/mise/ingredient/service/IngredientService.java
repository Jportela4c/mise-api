package br.com.mise.ingredient.service;

import br.com.mise.ingredient.dto.CreateIngredientRequest;
import br.com.mise.ingredient.dto.UpdateIngredientRequest;
import br.com.mise.ingredient.entity.Ingredient;
import br.com.mise.ingredient.exception.IngredientAlreadyExistsException;
import br.com.mise.ingredient.exception.IngredientNotFoundException;
import br.com.mise.ingredient.repository.IngredientRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@ApplicationScoped
public class IngredientService {

    private String toTitleCase(String str) {

        return Arrays.stream(str.trim().split("\\s+"))
                .map(text -> text.substring(0, 1).toUpperCase(Locale.ROOT)
                        + text.substring(1).toLowerCase(Locale.ROOT))
                .collect(Collectors.joining(" "));
    }

    @Inject
    IngredientRepository repository;

    @Transactional

    public Ingredient create(CreateIngredientRequest request) {
        String name = toTitleCase(request.name());
        String normalizedName = name.toLowerCase(Locale.ROOT);

        boolean alreadyExists = repository.count(
                "lower(name) = ?1",
                normalizedName) > 0;

        if (alreadyExists) {
            throw new IngredientAlreadyExistsException(name);
        }

        Ingredient ingredient = new Ingredient(name, request.unit());

        repository.persist(ingredient);
        return ingredient;
    }

    public Ingredient findById(Long id) {

        return repository
                .findByIdOptional(id)
                .orElseThrow(() -> new IngredientNotFoundException(id));
    }

    public List<Ingredient> findAll(){
        return repository
                .findAll(Sort.by("name"))
                .list();
    }

    @Transactional
    public Ingredient update(Long id, UpdateIngredientRequest request) {

        Ingredient ingredient = findById(id);

        String name = toTitleCase(request.name());
        String normalizedName = name.toLowerCase(Locale.ROOT);

        boolean alreadyExists = repository.count(
                "lower(name) = ?1 and id <> ?2",
                normalizedName,
                id
        ) > 0;

        if (alreadyExists) {
            throw new IngredientAlreadyExistsException(name);
        }

        ingredient.setName(name);
        ingredient.setUnit(request.unit());

        return ingredient;
    }

}
