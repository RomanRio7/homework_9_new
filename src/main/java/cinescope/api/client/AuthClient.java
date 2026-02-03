package cinescope.api.client;

import io.restassured.response.Response;
import cinescope.api.dto.LoginRequest;

import static io.restassured.RestAssured.given;
import static cinescope.api.spec.AuthSpecs.requestSpec;

public class AuthClient {

    public Response login(LoginRequest request) {
        return given()
                .spec(requestSpec())
                .body(request)
                .when()
                .post("/login");
    }
}