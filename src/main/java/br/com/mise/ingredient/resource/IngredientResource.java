package br.com.mise.ingredient.resource;

import br.com.mise.ingredient.dto.CreateIngredientRequest;
import br.com.mise.ingredient.dto.IngredientResponse;
import br.com.mise.ingredient.service.IngredientService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/api/v1/ingredients")
public class IngredientResource {

    @Inject
    IngredientService service;

    @POST
    public Response create(@Valid CreateIngredientRequest request) {

        var ingredient = service.create(request);

        return Response
                .status(Response.Status.CREATED)
                .entity(IngredientResponse.from(ingredient))
                .build();
    }
}