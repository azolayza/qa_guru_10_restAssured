package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public class BookStoreTests {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "https://demoqa.com";
    }

    @Test
    void getBookTest() {
        //curl -X GET "https://demoqa.com/BookStore/v1/Books" -H "accept: application/json"
        get("/BookStore/v1/Books")
                .then()
                .body("books", hasSize(greaterThan(0)));
    }

    @Test
    void getBooksWithAllLogsTest() {
        given()
                .log().uri()
                .log().body()
                .when()
                .get("/BookStore/v1/Books")
                .then()
                .log().status()
                .log().body()
                .body("books", hasSize(greaterThan(0)));
    }

    @Test
    void generateTokenTest() {
        String data = "{ \"userName\": \"alex\", " +
                "\"password\": \"asdsad#frew_DFS2\" }";
        given()
                .contentType(JSON)
                .body(data)
                .log().uri()
                .log().body()
                .when()
                .post("/Account/v1/GenerateToken")
                .then()
                .log().status()
                .log().body()
                .body("status", is("Success"))
                .body("result", is("User authorized successfully."))
                .body("token.size()", greaterThan(10));
    }

    @Test
    void generateTokenTestWithAllureListener() {
        String data = "{ \"userName\": \"alex\", " +
                "\"password\": \"asdsad#frew_DFS2\" }";
        given()
                .filter(new AllureRestAssured())
                .contentType(JSON)
                .body(data)
                .log().uri()
                .log().body()
                .when()
                .post("/Account/v1/GenerateToken")
                .then()
                .log().status()
                .log().body()
                .body("status", is("Success"))
                .body("result", is("User authorized successfully."))
                .body("token.size()", greaterThan(10));
    }

    @Test
    void generateTokenTestWithCustomAllureListener() {
        String data = "{ \"userName\": \"alex\", " +
                "\"password\": \"asdsad#frew_DFS2\" }";
        given()
                .filter(withCustomTemplates())
                .contentType(JSON)
                .body(data)
                .log().uri()
                .log().body()
                .when()
                .post("/Account/v1/GenerateToken")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/generateToken_response_shema.json"))
                .body("status", is("Success"))
                .body("result", is("User authorized successfully."))
                .body("token.size()", greaterThan(10));
    }
}
