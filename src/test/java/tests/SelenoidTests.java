package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsMapContaining.hasKey;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SelenoidTests {

    //make request to https://selenoid.autotests.cloud/status
    //check Total is 20

    @Test
    void checkTotal(){
        given()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .statusCode(200)
                .body("total", is(20));
    }

    @Test
    void checkTotalWithoutGiven(){
        get("https://selenoid.autotests.cloud/status")
                .then()
                .statusCode(200)
                .body("total", is(20));
    }

    @Test
    void checkTotalBadPractice(){
        String response = get("https://selenoid.autotests.cloud/status")
                .then()
                .statusCode(200)
                .extract().response().asString();

        System.out.println(response);
        String expectedResponse = "{\"total\":20,\"used\":0,\"queued\":0,\"pending\":0," +
                "\"browsers\":" +
                "{\"android\":{\"8.1\":{}}," +
                "\"chrome\":{\"90.0\":{},\"91.0\":{}}," +
                "\"firefox\":{\"88.0\":{},\"89.0\":{}}," +
                "\"opera\":{\"76.0\":{},\"77.0\":{}}}}\n";
        assertEquals(expectedResponse, response);
    }

    @Test
    void checkChromeVersion() {
        get("https://selenoid.autotests.cloud/status")
                .then()
                .statusCode(200)
                .body("browsers.chrome", hasKey("91.0"));
    }
    @Test
    void checkTotalGoodPractice() {
        Integer response =
                get("https://selenoid.autotests.cloud/status")
                        .then()
                        .statusCode(200)
                        .extract().path("total");

        System.out.println("Response: " + response);

        assertEquals(20, response);
    }

    @Test
    void responseExamples() {
        Response response =
                get("https://selenoid.autotests.cloud/status")
                        .then()
                        .extract().response();

        System.out.println("Response: " + response);
        System.out.println("Response .toString(): " + response.toString());
        System.out.println("Response .asString(): " + response.asString());
        System.out.println("Response .path(\"total\"): " + response.path("total"));
        System.out.println("Response .path(\"browsers.chrome\"): " + response.path("browsers.chrome"));
    }


    @Test
    void checkTotalWithAssertJ() {
        Integer response =
                get("https://selenoid.autotests.cloud/status")
                        .then()
                        .statusCode(200)
                        .extract().path("total");

        System.out.println("Response: " + response);

        assertThat(response).isEqualTo(20);
    }

    @Test
    void checkStatus401() {
        get("https://selenoid.autotests.cloud/ws/hub/status")
                        .then()
                        .statusCode(201);
    }
    //https://user1:1234@selenoid.autotests.cloud/ws/hub/status
    @Test
    void checkStatus200WithAuth() {
        given()
                .auth().basic("user1", "1234")
                .when()
                .get("https://user1:1234@selenoid.autotests.cloud/ws/hub/status")
                .then()
                .statusCode(200);
    }
}
