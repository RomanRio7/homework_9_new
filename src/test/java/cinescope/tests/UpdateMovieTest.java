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

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Cinescope API")
@Feature("Movies")
public class UpdateMovieTest {

    private final AuthApiSteps authSteps = new AuthApiSteps();
    private final MovieApiSteps movieApiSteps = new MovieApiSteps();
    private final MovieDbSteps movieDbSteps = new MovieDbSteps();

    @Test
    @Story("Обновление фильма")
    @DisplayName("PATCH /movies/{id} — успешное обновление фильма + проверка в БД")
    void updateMoviePositive() {
        String token = authSteps.loginAsAdmin();
        MovieResponse created = null;

        try {
            CreateMovieRequest request = CreateMovieRequest.builder()
                    .name("Movie update " + System.currentTimeMillis())
                    .price(170.0)
                    .description("Before update")
                    .location("SPB")
                    .published(true)
                    .genreId(1)
                    .build();

            created = movieApiSteps.createMovieSuccess(token, request);

            HashMap<String, Object> patchBody = new HashMap<>();
            String newName = "Movie updated " + System.currentTimeMillis();
            patchBody.put("name", newName);
            patchBody.put("price", 190.0);
            patchBody.put("description", "After update");

            MovieResponse updated = movieApiSteps.patchMovieSuccess(token, created.getId(), patchBody);

            assertThat(updated.getId()).isEqualTo(created.getId());
            assertThat(updated.getName()).isEqualTo(newName);
            assertThat(updated.getPrice()).isEqualTo(190.0);
            assertThat(updated.getDescription()).isEqualTo("After update");

            MovieDbModel dbMovie = movieDbSteps.getMovieById(created.getId());
            assertThat(dbMovie).isNotNull();
            assertThat(dbMovie.getName()).isEqualTo(newName);
            assertThat(dbMovie.getPrice()).isEqualTo(190.0);
            assertThat(dbMovie.getDescription()).isEqualTo("After update");
        } finally {
            if (created != null) {
                try {
                    movieApiSteps.deleteMovie(token, created.getId(), 204);
                } catch (AssertionError | Exception ignored) {
                }
            }
        }
    }

    @Test
    @Story("Обновление фильма — негатив")
    @DisplayName("PATCH /movies/{id} — фильм не найден → 404")
    void updateMovieNegative() {
        String token = authSteps.loginAsAdmin();

        HashMap<String, Object> patchBody = new HashMap<>();
        patchBody.put("name", "Does not exist");
        patchBody.put("price", 100.0);
        patchBody.put("description", "no movie");

        movieApiSteps.patchMovieError(token, 111_111_111L, patchBody, 404);
    }
}