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
public class DeleteMovieTest {

    private final AuthApiSteps authSteps = new AuthApiSteps();
    private final MovieApiSteps movieApiSteps = new MovieApiSteps();
    private final MovieDbSteps movieDbSteps = new MovieDbSteps();

    @Test
    @Story("Удаление фильма")
    @DisplayName("DELETE /movies/{id} — успешное удаление фильма + проверка в БД")
    void deleteMoviePositive() {
        String token = authSteps.loginAsAdmin();

        CreateMovieRequest request = CreateMovieRequest.builder()
                .name("Movie delete " + System.currentTimeMillis())
                .price(150.0)
                .description("To be deleted")
                .location("SPB")
                .published(true)
                .genreId(1)
                .build();

        MovieResponse created = movieApiSteps.createMovie(token, request, 201);

        movieApiSteps.deleteMovie(token, created.getId(), 200);

        MovieDbModel dbMovie = movieDbSteps.getMovieById(created.getId());
        assertThat(dbMovie)
                .as("Фильм с id=%s должен быть удалён из БД", created.getId())
                .isNull();
    }

    @Test
    @Story("Удаление фильма — негативный сценарий")
    @DisplayName("DELETE /movies/{id} — фильм не найден → 404")
    void deleteMovieNegative() {
        String token = authSteps.loginAsAdmin();

        movieApiSteps.deleteMovie(token, 999_999_999L, 404);
    }
}