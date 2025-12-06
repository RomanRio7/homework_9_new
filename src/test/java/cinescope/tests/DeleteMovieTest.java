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
public class DeleteMovieTest {

    private final AuthApiSteps authSteps = new AuthApiSteps();
    private final MovieApiSteps movieApiSteps = new MovieApiSteps();
    private final MovieDbSteps movieDbSteps = new MovieDbSteps();

    @Test
    @Story("Удаление фильма")
    @DisplayName("DELETE /movies/{id} — успешное удаление фильма + проверка в БД")
    void deleteMoviePositive() {
        String token = authSteps.loginAsAdmin();

        CreateMovieRequest request = new CreateMovieRequest();
        request.setName("Movie delete " + System.currentTimeMillis());
        request.setPrice(150.0);
        request.setDescription("To be deleted");
        request.setLocation("SPB");
        request.setPublished(true);
        request.setGenreId(1);

        MovieResponse created = movieApiSteps.createMovie(token, request, 201);

        movieApiSteps.deleteMovie(token, created.getId(), 200);

        movieDbSteps.assertMovieNotExists(created.getId());
    }

    @Test
    @Story("Удаление фильма — негативный")
    @DisplayName("DELETE /movies/{id} — фильм не найден → 404")
    void deleteMovieNegative() {
        String token = authSteps.loginAsAdmin();
        movieApiSteps.deleteMovie(token, 999_999_999L, 404);
    }
}