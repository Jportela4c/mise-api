
package br.com.mise.ingredient.resource;

import br.com.mise.ingredient.entity.Ingredient;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class IngredientRepository
        implements PanacheRepository<Ingredient> {
}
