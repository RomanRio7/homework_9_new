package cinescope.tests;

import cinescope.api.dto.CreateMovieRequest;
import cinescope.api.dto.MovieResponse;
import cinescope.steps.AuthApiSteps;
import cinescope.steps.MovieApiSteps;
import cinescope.db.model.MovieDbModel;
import cinescope.steps.MovieDbSteps;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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

        CreateMovieRequest request = CreateMovieRequest.builder()
                .name("Movie " + System.currentTimeMillis())
                .imageUrl("https://image.url")
                .price(200.0)
                .description("Test movie")
                .location("MSK")
                .published(true)
                .genreId(1)
                .build();

        MovieResponse response = movieApiSteps.createMovieSuccess(token, request);

        try {
            MovieDbModel dbMovie = movieDbSteps.getMovieById(response.getId());

            assertThat(dbMovie)
                    .as("Фильм должен существовать в БД")
                    .isNotNull();

            assertThat(dbMovie.getId()).isEqualTo(response.getId());
            assertThat(dbMovie.getName()).isEqualTo(request.getName());
            assertThat(dbMovie.getPrice()).isEqualTo(request.getPrice());
            assertThat(dbMovie.getDescription()).isEqualTo(request.getDescription());
            assertThat(dbMovie.getLocation()).isEqualTo(request.getLocation());
            assertThat(dbMovie.getPublished()).isEqualTo(request.getPublished());
            assertThat(dbMovie.getGenreId()).isEqualTo(request.getGenreId());
        } finally {
            movieApiSteps.deleteMovie(token, response.getId(), 200);
        }
    }

    @Test
    @Story("Создание фильма — негативный сценарий")
    @DisplayName("POST /movies — создание фильма с пустым name → 400")
    void createMovieNegative() {
        String token = authSteps.loginAsAdmin();

        CreateMovieRequest request = CreateMovieRequest.builder()
                .name("")
                .price(200.0)
                .description("Test movie")
                .location("MSK")
                .genreId(1)
                .build();

        movieApiSteps.createMovieError(token, request, 400);
    }
}