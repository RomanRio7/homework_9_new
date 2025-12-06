package cinescope.tests;

import cinescope.api.dto.CreateMovieRequest;
import cinescope.api.dto.MovieResponse;
import cinescope.api.steps.AuthApiSteps;
import cinescope.api.steps.MovieApiSteps;
import cinescope.db.steps.MovieDbSteps;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Cinescope API")
@Feature("Movies")
public class GetMovieTest {

    private final AuthApiSteps authSteps = new AuthApiSteps();
    private final MovieApiSteps movieApiSteps = new MovieApiSteps();
    private final MovieDbSteps movieDbSteps = new MovieDbSteps();

    @Test
    @Story("Получение фильма")
    @DisplayName("GET /movies/{id} — успешное получение фильма + сверка с БД")
    void getMoviePositive() {
        String token = authSteps.loginAsAdmin();

        CreateMovieRequest request = new CreateMovieRequest();
        request.setName("Movie for GET " + System.currentTimeMillis());
        request.setPrice(180.0);
        request.setDescription("Get test");
        request.setLocation("MSK");
        request.setPublished(true);
        request.setGenreId(1);

        MovieResponse created = movieApiSteps.createMovie(token, request, 201);

        MovieResponse got = movieApiSteps.getMovie(token, created.getId(), 200);

        assertThat(got.getId()).isEqualTo(created.getId());
        assertThat(got.getName()).isEqualTo(request.getName());
        assertThat(got.getPrice()).isEqualTo(request.getPrice());
        assertThat(got.getLocation()).isEqualTo(request.getLocation());
        assertThat(got.getPublished()).isTrue();

        movieDbSteps.assertMovieExists(created.getId());
    }

    @Test
    @Story("Получение фильма — негатив")
    @DisplayName("GET /movies/{id} — несуществующий ID → 404")
    void getMovieNegative() {
        String token = authSteps.loginAsAdmin();
        movieApiSteps.getMovie(token, 999_999_999L, 404);
    }
}