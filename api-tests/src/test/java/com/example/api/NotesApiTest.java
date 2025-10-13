package com.example.api;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import java.util.Map;
import java.util.UUID;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NotesApiTest {
    static String BASE, id;

    @BeforeAll static void init() {
        BASE = System.getProperty("baseUrl", "http://localhost:8080");
    }

    @Test @Order(1)
    void health() {
        given().baseUri(BASE).when().get("/health")
                .then().statusCode(200).body("status", equalTo("ok"));
    }

    @Test @Order(2)
    void create() {
        var title = "Note " + UUID.randomUUID();
        id = given().baseUri(BASE).contentType(ContentType.JSON)
                .body(Map.of("title", title, "content", "hello"))
                .when().post("/notes")
                .then().statusCode(201)
                .header("Location", containsString("/notes/"))
                .body("id", notNullValue())
                .body("title", equalTo(title))
                .body("createdAt", notNullValue())
                .extract().jsonPath().getString("id");
    }

    @Test @Order(3)
    void getById() {
        given().baseUri(BASE).when().get("/notes/" + id)
                .then().statusCode(200).body("id", equalTo(id));
    }

    @Test @Order(4)
    void list() {
        given().baseUri(BASE).when().get("/notes")
                .then().statusCode(200).body("$", not(empty()));
    }

    @Test @Order(5)
    void update() {
        given().baseUri(BASE).contentType(ContentType.JSON)
                .body(Map.of("content", "updated"))
                .when().put("/notes/" + id)
                .then().statusCode(200).body("content", equalTo("updated"));
    }

    @Test @Order(6)
    void deleteAnd404() {
        given().baseUri(BASE).when().delete("/notes/" + id).then().statusCode(204);
        given().baseUri(BASE).when().get("/notes/" + id).then().statusCode(404);
    }
}
