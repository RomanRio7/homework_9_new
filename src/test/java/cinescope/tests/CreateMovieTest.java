package cinescope.tests;

import cinescope.api.dto.CreateMovieRequest;
import cinescope.api.dto.MovieResponse;
import cinescope.api.steps.AuthApiSteps;
import cinescope.api.steps.MovieApiSteps;
import cinescope.db.steps.MovieDbSteps;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Epic("Cinescope API")
@Feature("Movies")
public class CreateMovieTest {

    private final AuthApiSteps authSteps = new AuthApiSteps();
    private final MovieApiSteps movieApiSteps = new MovieApiSteps();
    private final MovieDbSteps movieDbSteps = new MovieDbSteps();

    @Test
    @Story("Создание фильма")
    @DisplayName("POST /movies — успешное создание фильма + проверка в БД")
    void createMoviePositive() {
        String token = authSteps.loginAsAdmin();

        CreateMovieRequest request = new CreateMovieRequest();
        request.setName("Movie " + System.currentTimeMillis());
        request.setImageUrl("https://image.url");
        request.setPrice(200.0);
        request.setDescription("Test movie");
        request.setLocation("MSK");
        request.setPublished(true);
        request.setGenreId(1);

        MovieResponse response =
                movieApiSteps.createMovie(token, request, 201);

        movieDbSteps.assertMovieExists(response.getId());
    }

    @Test
    @Story("Создание фильма — негативный сценарий")
    @DisplayName("POST /movies — создание фильма с пустым name → 400")
    void createMovieNegative() {
        String token = authSteps.loginAsAdmin();

        CreateMovieRequest request = new CreateMovieRequest();
        request.setName("");
        request.setPrice(200.0);
        request.setDescription("Test movie");
        request.setLocation("MSK");
        request.setGenreId(1);

        movieApiSteps.createMovie(token, request, 400);
    }
}