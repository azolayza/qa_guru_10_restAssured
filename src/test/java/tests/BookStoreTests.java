package tests;

import io.qameta.allure.Story;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public class BookStoreTests {

    @BeforeAll
    @Tag("bookstoretest")
    static void setup() {
        RestAssured.baseURI = "https://demoqa.com";
    }

    @Story("API тесты для demoqa.com/bookstore")
    @DisplayName("Проверка конкретной книги по номеру ISBN")
    @Test
    @Tag("bookstoretest")
    void getAnyBookTest() {
        given()
                .filter(withCustomTemplates())
                .params("ISBN", "9781449337711")//
                .log().uri()
                .log().body()
                .when()
                .get("/BookStore/v1/Book")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("title", is("Designing Evolvable Web APIs with ASP.NET"))
                .body("author", is("Glenn Block et al."));
    }

    @Story("API тесты для demoqa.com/bookstore")
    @DisplayName("Проверка полного списка книг")
    @Test
    @Tag("bookstoretest")
    void getBooksTest() {
        given()
                .filter(withCustomTemplates())
                .log().all()
                .when()
                .get("/BookStore/v1/Books")
                .then()
                .log().all()
                .body("books", hasSize(greaterThan(0)))
                .body("books.title[0]", is("Git Pocket Guide"));
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

    @Story("API тесты для demoqa.com/bookstore")
    @DisplayName("Проверка генерации токена пользователя")
    @Test
    @Tag("bookstoretest")
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

    @Story("API тесты для demoqa.com/bookstore")
    @DisplayName("Проверка генерации токена пользователя с помощью AllureListener")
    @Test
    @Tag("bookstoretest")
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

    @Story("API тесты для demoqa.com/bookstore")
    @DisplayName("Проверка генерации токена пользователя с помощью CustomAllureListener")
    @Test
    @Tag("bookstoretest")
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
