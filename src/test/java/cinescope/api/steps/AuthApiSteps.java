package cinescope.api.steps;

import cinescope.api.client.AuthClient;
import cinescope.api.dto.LoginRequest;
import cinescope.api.dto.LoginResponse;
import cinescope.util.TestConfig;
import io.qameta.allure.Step;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthApiSteps {

    private final AuthClient authClient;

    public AuthApiSteps() {
        this(new AuthClient());
    }

    public AuthApiSteps(AuthClient authClient) {
        this.authClient = authClient;
    }

    @Step("Логин: {email}")
    public String loginAndGetToken(String email, String password) {
        LoginRequest request = LoginRequest.builder()
                .email(email)
                .password(password)
                .build();

        LoginResponse response = authClient.login(request)
                .then()
                .statusCode(200)
                .extract()
                .as(LoginResponse.class);

        assertThat(response.getAccessToken())
                .as("Access token должен быть заполнен")
                .isNotBlank();

        return response.getAccessToken();
    }

    @Step("Логин как админ")
    public String loginAsAdmin() {
        String email = TestConfig.get("cinescope.admin.email");
        String password = TestConfig.get("cinescope.admin.password");
        return loginAndGetToken(email, password);
    }
}