package org.ikoinaris.microservices;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasKey;

@QuarkusTest
public class NumberResourceTest {

    @Test
    public void shouldGenerateIsbnNumbers() {
        given()
          .when().get("/api/numbers")
          .then()
          .statusCode(200)
          .body("isbn_13", startsWith("13-"))
          .body("isbn_10", startsWith("10-"))
          .body(not(hasKey("generationDate")));

    }
}
