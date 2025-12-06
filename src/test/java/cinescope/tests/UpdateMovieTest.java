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
public class UpdateMovieTest {

    private final AuthApiSteps authSteps = new AuthApiSteps();
    private final MovieApiSteps movieApiSteps = new MovieApiSteps();
    private final MovieDbSteps movieDbSteps = new MovieDbSteps();

    @Test
    @Story("Обновление фильма")
    @DisplayName("PATCH /movies/{id} — успешное обновление фильма + проверка в БД")
    void updateMoviePositive() {
        String token = authSteps.loginAsAdmin();

        // создаём
        CreateMovieRequest request = new CreateMovieRequest();
        request.setName("Movie update " + System.currentTimeMillis());
        request.setPrice(170.0);
        request.setDescription("Before update");
        request.setLocation("SPB");
        request.setPublished(true);
        request.setGenreId(1);

        MovieResponse created = movieApiSteps.createMovie(token, request, 201);

        // тело для PATCH
        var patchBody = new java.util.HashMap<String, Object>();
        String newName = "Movie updated " + System.currentTimeMillis();
        patchBody.put("name", newName);
        patchBody.put("price", 190.0);
        patchBody.put("description", "After update");

        MovieResponse updated = movieApiSteps.patchMovie(token, created.getId(), patchBody, 200);

        // проверки по API
        assertThat(updated.getId()).isEqualTo(created.getId());
        assertThat(updated.getName()).isEqualTo(newName);
        assertThat(updated.getPrice()).isEqualTo(190.0);
        assertThat(updated.getDescription()).isEqualTo("After update");

        // 🔥 проверки в БД
        movieDbSteps.assertUpdatedField(created.getId(), "name", newName);
        movieDbSteps.assertUpdatedField(created.getId(), "price", 190.0);
        movieDbSteps.assertUpdatedField(created.getId(), "description", "After update");
    }

    @Test
    @Story("Обновление фильма — негатив")
    @DisplayName("PATCH /movies/{id} — фильм не найден → 404")
    void updateMovieNegative() {
        String token = authSteps.loginAsAdmin();

        var patchBody = new java.util.HashMap<String, Object>();
        patchBody.put("name", "Does not exist");
        patchBody.put("price", 100.0);
        patchBody.put("description", "no movie");

        movieApiSteps.patchMovie(token, 111_111_111L, patchBody, 404);
    }
}