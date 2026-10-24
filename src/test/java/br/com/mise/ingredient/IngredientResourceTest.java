package br.com.mise.ingredient;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class IngredientResourceTest {

    @Test
    public void shouldCreateIngredient() {

        String request = """
                {
                    "name": "Batata",
                    "unit": "GRAM"
                }
                """;

        given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/api/v1/ingredients")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo("Batata"))
                .body("unit", equalTo("GRAM"));
    }

    @Test
    public void shouldRejectDuplicateIngredient() {

        String firstRequest = """
            {
                "name": "Cenoura",
                "unit": "GRAM"
            }
            """;

        String duplicateRequest = """
            {
                "name": "CENOURA",
                "unit": "GRAM"
            }
            """;

        given()
                .contentType("application/json")
                .body(firstRequest)
                .when()
                .post("/api/v1/ingredients")
                .then()
                .statusCode(201);

        given()
                .contentType("application/json")
                .body(duplicateRequest)
                .when()
                .post("/api/v1/ingredients")
                .then()
                .statusCode(409)
                .body("code", equalTo("INGREDIENT_ALREADY_EXISTS"));
    }


    @Test
    public void shouldRejectBlankName() {

        String request = """
            {
                "name": "",
                "unit": "GRAM"
            }
            """;

        given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/api/v1/ingredients")
                .then()
                .statusCode(400)
                .body("code", equalTo("VALIDATION_ERROR"))
                .body("message", equalTo("name: must not be blank"));
    }


    @Test
    public void shouldRejectNullUnit() {

        String request = """
            {
                "name": "Tomate",
                "unit": null
            }
            """;

        given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/api/v1/ingredients")
                .then()
                .statusCode(400)
                .body("code", equalTo("VALIDATION_ERROR"))
                .body("message", equalTo("unit: must not be null"));
    }

    @Test
    public void shouldFindIngredientById() {

        String request = """
            {
                "name": "Abobrinha",
                "unit": "GRAM"
            }
            """;

        Long id = given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/api/v1/ingredients")
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()
                .getLong("id");

        given()
                .pathParam("id", id)
                .when()
                .get("/api/v1/ingredients/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(id.intValue()))
                .body("name", equalTo("Abobrinha"))
                .body("unit", equalTo("GRAM"));
    }


    @Test
    public void shouldReturnNotFoundForUnknownId() {

        given()
                .pathParam("id", Long.MAX_VALUE)
                .when()
                .get("/api/v1/ingredients/{id}")
                .then()
                .statusCode(404)
                .body("code", equalTo("INGREDIENT_NOT_FOUND"));
    }


    @Test
    public void shouldListIngredients() {

        String request = """
            {
                "name": "Berinjela",
                "unit": "GRAM"
            }
            """;

        given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/api/v1/ingredients")
                .then()
                .statusCode(201);

        given()
                .when()
                .get("/api/v1/ingredients")
                .then()
                .statusCode(200)
                .body("name", hasItem("Berinjela"));
    }


    @Test
    void shouldUpdateIngredient() {

        Long id = given()
                .contentType(ContentType.JSON)
                .body("""
                    {
                      "name": "LEITE",
                      "unit": "GRAM"
                    }
                    """)
                .when()
                .post("/api/v1/ingredients")
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()
                .getLong("id");

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", id)
                .body("""
                    {
                      "name": "LEITE INTEGRAL",
                      "unit": "MILLILITER"
                    }
                    """)
                .when()
                .put("/api/v1/ingredients/{id}")
                .then()
                .statusCode(200)
                .body("name", equalTo("Leite Integral"))
                .body("unit", equalTo("MILLILITER"));

        given()
                .pathParam("id", id)
                .when()
                .get("/api/v1/ingredients/{id}")
                .then()
                .statusCode(200)
                .body("name", equalTo("Leite Integral"))
                .body("unit", equalTo("MILLILITER"));
    }


    @Test
    void shouldReturn404WhenUpdatingNonexistentIngredient() {

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", Long.MAX_VALUE)
                .body("""
                    {
                      "name": "LEITE",
                      "unit": "MILLILITER"
                    }
                    """)
                .when()
                .put("/api/v1/ingredients/{id}")
                .then()
                .statusCode(404)
                .body("code", equalTo("INGREDIENT_NOT_FOUND"));
    }


    @Test
    void shouldReturn409WhenUpdatingToExistingName() {

        given()
                .contentType(ContentType.JSON)
                .body("""
                    {
                      "name": "CENOURA TESTE DUPLICIDADE",
                      "unit": "GRAM"
                    }
                    """)
                .when()
                .post("/api/v1/ingredients")
                .then()
                .statusCode(201);

        Long id = given()
                .contentType(ContentType.JSON)
                .body("""
                    {
                      "name": "BATATA TESTE DUPLICIDADE",
                      "unit": "GRAM"
                    }
                    """)
                .when()
                .post("/api/v1/ingredients")
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()
                .getLong("id");

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", id)
                .body("""
                    {
                      "name": "cenoura teste duplicidade",
                      "unit": "GRAM"
                    }
                    """)
                .when()
                .put("/api/v1/ingredients/{id}")
                .then()
                .statusCode(409)
                .body("code", equalTo("INGREDIENT_ALREADY_EXISTS"));
    }


    @Test
    void shouldReturn400WhenUpdatingWithBlankName() {

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", Long.MAX_VALUE)
                .body("""
                    {
                      "name": "   ",
                      "unit": "GRAM"
                    }
                    """)
                .when()
                .put("/api/v1/ingredients/{id}")
                .then()
                .statusCode(400)
                .body("code", equalTo("VALIDATION_ERROR"));
    }


    @Test
    void shouldReturn400WhenUpdatingWithNullUnit() {

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", Long.MAX_VALUE)
                .body("""
                    {
                      "name": "Leite Integral",
                      "unit": null
                    }
                    """)
                .when()
                .put("/api/v1/ingredients/{id}")
                .then()
                .statusCode(400)
                .body("code", equalTo("VALIDATION_ERROR"));
    }



    @Test
    void shouldAllowUpdatingIngredientWithSameName() {

        Long id = given()
                .contentType(ContentType.JSON)
                .body("""
                    {
                      "name": "ABOBORA TESTE MESMO NOME",
                      "unit": "GRAM"
                    }
                    """)
                .when()
                .post("/api/v1/ingredients")
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()
                .getLong("id");

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", id)
                .body("""
                    {
                      "name": "abobora teste mesmo nome",
                      "unit": "UNIT"
                    }
                    """)
                .when()
                .put("/api/v1/ingredients/{id}")
                .then()
                .statusCode(200)
                .body("name", equalTo("Abobora Teste Mesmo Nome"))
                .body("unit", equalTo("UNIT"));
    }


    @Test
    void shouldNormalizeIngredientNameOnCreate() {

        given()
                .contentType(ContentType.JSON)
                .body("""
                    {
                      "name": "  FARINHA   de   AVEIA  ",
                      "unit": "GRAM"
                    }
                    """)
                .when()
                .post("/api/v1/ingredients")
                .then()
                .statusCode(201)
                .body("name", equalTo("Farinha De Aveia"))
                .body("unit", equalTo("GRAM"));
    }


    @Test
    void shouldDeleteIngredient() {

        Long id = given()
                .contentType(ContentType.JSON)
                .body("""
                    {
                      "name": "INGREDIENTE TESTE EXCLUSAO",
                      "unit": "GRAM"
                    }
                    """)
                .when()
                .post("/api/v1/ingredients")
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()
                .getLong("id");

        given()
                .pathParam("id", id)
                .when()
                .delete("/api/v1/ingredients/{id}")
                .then()
                .statusCode(204);

        given()
                .pathParam("id", id)
                .when()
                .get("/api/v1/ingredients/{id}")
                .then()
                .statusCode(404)
                .body("code", equalTo("INGREDIENT_NOT_FOUND"));
    }

    @Test
    void shouldReturn404WhenDeletingNonexistentIngredient() {

        given()
                .pathParam("id", Long.MAX_VALUE)
                .when()
                .delete("/api/v1/ingredients/{id}")
                .then()
                .statusCode(404)
                .body("code", equalTo("INGREDIENT_NOT_FOUND"));
    }







}

