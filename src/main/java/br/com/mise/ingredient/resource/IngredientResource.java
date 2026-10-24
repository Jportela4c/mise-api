package br.com.mise.ingredient.resource;

import br.com.mise.ingredient.dto.CreateIngredientRequest;
import br.com.mise.ingredient.dto.IngredientResponse;
import br.com.mise.ingredient.dto.UpdateIngredientRequest;
import br.com.mise.ingredient.entity.Ingredient;
import br.com.mise.ingredient.service.IngredientService;
import io.quarkus.panache.common.Sort;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/v1/ingredients")
public class IngredientResource {

    @Inject
    IngredientService service;

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        Ingredient ingredient = service.findById(id);

        IngredientResponse response = IngredientResponse.from(ingredient);

        return Response
                .ok(response)
                .build();
    }

    @GET
    public Response findAll() {
        List<Ingredient> ingredients = service.findAll();

        List<IngredientResponse> response = ingredients
                .stream()
                .map(IngredientResponse::from) //.map(ingredient -> IngredientResponse.from(ingredient))
                .toList();

        return Response
                .ok(ingredients)
                .build();
    }

    @POST
    public Response create(@Valid CreateIngredientRequest request) {

        var ingredient = service.create(request);

        return Response
                .status(Response.Status.CREATED)
                .entity(IngredientResponse.from(ingredient))
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response update(
            @PathParam("id") Long id,
            @Valid UpdateIngredientRequest request
    ) {
        Ingredient ingredient = service.update(id, request);
        IngredientResponse response = IngredientResponse.from(ingredient);

        return Response
                .ok(response)
                .build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        service.delete(id);

        return Response.noContent().build();
    }
}