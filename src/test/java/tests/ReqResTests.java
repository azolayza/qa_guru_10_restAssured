package tests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReqResTests {

    @Test
    void successfulLogin(){
        /*
        Request url: "https://reqres.in/api/login"
        Data:
        {
            "email": "eve.holt@reqres.in",
            "password": "cityslicka"
         }
         Response
         {
             "token": "QpwL5tke4Pnpja7X4"
         }
        */
        String data = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\" }";
        given()
                .contentType(JSON)
                .body(data)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X4"));

    }

    @Test
    void negativeLogin(){
        /*
        Request url: "https://reqres.in/api/login"
        Data:
        {
            "email": "peter@klaven"
        }
         Response status 400
         {
              "error": "Missing password"
        }
        */
        String data = "{ \"email\": \"peter@klaven\" }";
        given()
                .contentType(JSON)
                .body(data)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .statusCode(400)
                .body("error", is("Missing password"));

    }
//HomeWork

    @Test
    void successfulRegistration(){
        /*
        Request url: "https://reqres.in/api/register"
        Data:
        {
            "email": "eve.holt@reqres.in",
            "password": "pistol"
        }
         Response
         {
            "id": 4,
            "token": "QpwL5tke4Pnpja7X4"
         }
        */
        String data = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\" }";
        given()
                .contentType(JSON)
                .body(data)
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    void negativeRegistration(){
        /*
        Request url: "https://reqres.in/api/register"
        Data:
        {
            "email": "sydney@fife"
        }
         Response status 400
         {
              "error": "Missing password"
        }
        */
        String data = "{ \"email\": \"sydney@fife\" }";
        given()
                .contentType(JSON)
                .body(data)
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .statusCode(400)
                .body("error", is("Missing password"));

    }

    //https://reqres.in/api/unknown/2

    @Test
    void checkUserIdTest(){
        /* Request url: "https://reqres.in/api/unknown/2"
        Method% GET
        Response
         { "data": {
            "id": 2,
            "name": "fuchsia rose",
            "year": 2001,
            "color": "#C74375",
            "pantone_value": "17-2031"
            },
            "support": {
            "url": "https://reqres.in/#support-heading",
            "text": "To keep ReqRes free, contributions towards server costs are appreciated!"}*/

        Integer response =
                get("https://reqres.in/api/unknown/2")
                        .then()
                        .statusCode(200)
                        .extract().path("data.id");

        System.out.println("USER ID is : " + response);
        assertEquals(2, response);
    }

    @Test
    void userNotFoundTest(){
        /*  Request url: "https://reqres.in/api/users/23"
            Method: GET
            Response: Status 404
            { }
         */
        String requestUri = "https://reqres.in/api/users/23";
        given()
                .when()
                .get("https://reqres.in/api/users/23")
                .then()
                .statusCode(404);
        System.out.println("User not found from link: " +requestUri);
    }

    @Test
    void checkTotalUsersFromList() {
    /*  Request url: "https://reqres.in/api/unknown3"
            Method: GET
            Response: Status 200
            {reqres_list_users.json}
     */
        Integer response =
                get("https://reqres.in/api/unknown")
                        .then()
                        .statusCode(200)
                        .extract().path("total");

        System.out.println("Response Total: " + response);

        assertEquals(12, response);
    }

    @Test
    void successfulUpdateUser(){
        /* Check data update
        Request url: "https://reqres.in/api/users/2"
        Method: PUT
        Data:
        {
            "name": "morpheus",
            "job": "zion resident"
        }
         Response
         {
            "name": "morpheus",
            "job": "zion resident",
            "updatedAt": "2022-02-26T18:16:01.298Z"
         }
        */
        String data = "{ \"name\": \"morpheus\", \"job\": \"zion resident\" }";
        given()
                .contentType(JSON)
                .body(data)
                .when()
                .put("https://reqres.in/api/users/2")
                .then()
                .statusCode(200)
                .body("updatedAt", containsString("2022-02"));
    }

}
