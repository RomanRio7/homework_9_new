package cinescope.api.steps;

import cinescope.api.client.AuthClient;
import cinescope.api.dto.LoginRequest;
import cinescope.api.dto.LoginResponse;
import io.restassured.response.Response;

public class AuthApiSteps {

    private final AuthClient authClient = new AuthClient();

    public String loginAndGetToken(String email, String password) {
        LoginRequest request = LoginRequest.builder()
                .email(email)
                .password(password)
                .build();

        Response response = authClient.login(request);
        response.then().statusCode(200);

        LoginResponse loginResponse = response.as(LoginResponse.class);
        if (loginResponse.getAccessToken() == null || loginResponse.getAccessToken().isBlank()) {
            throw new AssertionError("Access token is empty in login response");
        }
        return loginResponse.getAccessToken();
    }

    public String loginAsAdmin() {
        String email = "roman_sarsengaliev@mail.ru";
        String password = "baXfo8-nyptyc-jicxob";
        return loginAndGetToken(email, password);
    }
}