package br.com.mise.ingredient;

import io.quarkus.test.junit.QuarkusTest;
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




}

