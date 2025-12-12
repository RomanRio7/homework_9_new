package cinescope.tests;

import cinescope.api.dto.FindAllMoviesResponse;
import cinescope.api.dto.CreateMovieRequest;
import cinescope.api.dto.MovieResponse;
import cinescope.api.steps.AuthApiSteps;
import cinescope.api.steps.MovieApiSteps;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Cinescope API")
@Feature("Movies")
public class GetMoviesListTest {

    private final AuthApiSteps authSteps = new AuthApiSteps();
    private final MovieApiSteps movieApiSteps = new MovieApiSteps();

    @Test
    @Story("Получение списка фильмов")
    @DisplayName("GET /movies — 10 фильмов на странице при pageSize=10")
    void getMoviesList() {
        String token = authSteps.loginAsAdmin();

        List<Long> createdIds = new ArrayList<>();

        try {
            for (int i = 0; i < 10; i++) {
                CreateMovieRequest request = CreateMovieRequest.builder()
                        .name("Movie list auto " + UUID.randomUUID())
                        .price(120.0)
                        .description("List test")
                        .location("MSK")
                        .published(true)
                        .genreId(1)
                        .build();

                MovieResponse created = movieApiSteps.createMovie(token, request, 201);
                createdIds.add(created.getId());
            }

            FindAllMoviesResponse list =
                    movieApiSteps.getMovies(token, 1, 10, 200);

            assertThat(list.getMovies())
                    .as("На первой странице должно быть ровно 10 фильмов")
                    .hasSize(10);

            assertThat(list.getPage()).isEqualTo(1);
            assertThat(list.getPageSize()).isEqualTo(10);
            assertThat(list.getPageCount()).isGreaterThanOrEqualTo(1);
            assertThat(list.getCount()).isGreaterThanOrEqualTo(10);
        } finally {
            for (Long id : createdIds) {
                try {
                    movieApiSteps.deleteMovie(token, id, 200);
                } catch (AssertionError | Exception ignored) {
                }
            }
        }
    }
}