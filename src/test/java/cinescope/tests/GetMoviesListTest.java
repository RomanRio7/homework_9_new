package cinescope.tests;

import cinescope.api.dto.FindAllMoviesResponse;
import cinescope.api.dto.CreateMovieRequest;
import cinescope.api.steps.AuthApiSteps;
import cinescope.api.steps.MovieApiSteps;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Cinescope API")
@Feature("Movies")
public class GetMoviesListTest {

    private final AuthApiSteps authSteps = new AuthApiSteps();
    private final MovieApiSteps movieApiSteps = new MovieApiSteps();

    @Test
    @Story("Получение списка фильмов")
    @DisplayName("GET /movies — базовая проверка списка + пагинация")
    void getMoviesList() {
        String token = authSteps.loginAsAdmin();

        CreateMovieRequest request = new CreateMovieRequest();
        request.setName("Movie list " + System.currentTimeMillis());
        request.setPrice(120.0);
        request.setDescription("List test");
        request.setLocation("MSK");
        request.setPublished(true);
        request.setGenreId(1);
        movieApiSteps.createMovie(token, request, 201);

        FindAllMoviesResponse list = movieApiSteps.getMovies(token, 1, 10, 200);

        assertThat(list.getMovies()).isNotEmpty();
        assertThat(list.getPage()).isEqualTo(1);
        assertThat(list.getPageSize()).isEqualTo(10);
        assertThat(list.getCount()).isGreaterThan(0);
    }
}