package org.ikoinaris.microservices;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;

@QuarkusTest
public class BookResourceTest {

    @Test
    public void shouldCreateBook() {
        given()
          .formParam("title", "Microservices with Quarkus")
          .formParam("author", "Ioannis Koinaris")
          .formParam("year", 2021)
          .formParam("genre", "IT")
        .when()
           .post("/api/books")
        .then()
          .statusCode(201)
          .body("isbn_13", startsWith("13-"))
          .body("title", is("Microservices with Quarkus"))
          .body("author", is("Ioannis Koinaris"))
          .body("year_Of_Publication", is(2021))
          .body("genre", is("IT"))
          .body("creation_date", startsWith("20"));
    }
}
