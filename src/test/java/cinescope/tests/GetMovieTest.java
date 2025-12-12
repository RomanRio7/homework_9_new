package cinescope.tests;

import cinescope.api.dto.CreateMovieRequest;
import cinescope.api.dto.MovieResponse;
import cinescope.api.steps.AuthApiSteps;
import cinescope.api.steps.MovieApiSteps;
import cinescope.db.model.MovieDbModel;
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
        MovieResponse created = null;

        try {
            CreateMovieRequest request = CreateMovieRequest.builder()
                    .name("Movie for GET " + System.currentTimeMillis())
                    .price(180.0)
                    .description("Get test")
                    .location("MSK")
                    .published(true)
                    .genreId(1)
                    .build();

            created = movieApiSteps.createMovie(token, request, 201);

            MovieResponse got = movieApiSteps.getMovie(token, created.getId(), 200);

            assertThat(got.getId()).isEqualTo(created.getId());
            assertThat(got.getName()).isEqualTo(request.getName());
            assertThat(got.getPrice()).isEqualTo(request.getPrice());
            assertThat(got.getLocation()).isEqualTo(request.getLocation());
            assertThat(got.getPublished()).isTrue();

            MovieDbModel dbMovie = movieDbSteps.getMovieById(created.getId());
            assertThat(dbMovie).isNotNull();
            assertThat(dbMovie.getId()).isEqualTo(created.getId());
            assertThat(dbMovie.getName()).isEqualTo(request.getName());
            assertThat(dbMovie.getPrice()).isEqualTo(request.getPrice());
            assertThat(dbMovie.getLocation()).isEqualTo(request.getLocation());
            assertThat(dbMovie.getPublished()).isEqualTo(request.getPublished());
            assertThat(dbMovie.getGenreId()).isEqualTo(request.getGenreId());
        } finally {
            if (created != null) {
                try {
                    movieApiSteps.deleteMovie(token, created.getId(), 200);
                } catch (AssertionError | Exception ignored) {
                }
            }
        }
    }

    @Test
    @Story("Получение фильма — негатив")
    @DisplayName("GET /movies/{id} — несуществующий ID → 404")
    void getMovieNegative() {
        String token = authSteps.loginAsAdmin();
        movieApiSteps.getMovie(token, 999_999_999L, 404);
    }
}