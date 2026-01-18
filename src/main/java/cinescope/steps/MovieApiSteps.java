package cinescope.steps;

import cinescope.api.client.MovieClient;
import cinescope.api.dto.CreateMovieRequest;
import cinescope.api.dto.FindAllMoviesResponse;
import cinescope.api.dto.MovieResponse;
import io.qameta.allure.Step;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class MovieApiSteps {

    private final MovieClient movieClient;

    public MovieApiSteps() {
        this(new MovieClient());
    }

    public MovieApiSteps(MovieClient movieClient) {
        this.movieClient = movieClient;
    }

    @Step("Создать фильм (успех)")
    public MovieResponse createMovieSuccess(String token, CreateMovieRequest request) {
        MovieResponse movie = movieClient.createMovie(token, request)
                .then()
                .statusCode(201)
                .extract()
                .as(MovieResponse.class);

        assertMovieMatchesCreateRequest(movie, request);
        return movie;
    }

    @Step("Создать фильм (ожидаем ошибку {expectedStatus})")
    public void createMovieError(String token, CreateMovieRequest request, int expectedStatus) {
        movieClient.createMovie(token, request)
                .then()
                .statusCode(expectedStatus);
    }

    @Step("Получить фильм id={id} (успех)")
    public MovieResponse getMovieSuccess(String token, long id) {
        return movieClient.getMovie(token, id)
                .then()
                .statusCode(200)
                .extract()
                .as(MovieResponse.class);
    }

    @Step("Получить фильм id={id} (ожидаем статус {expectedStatus})")
    public void getMovieError(String token, long id, int expectedStatus) {
        movieClient.getMovie(token, id)
                .then()
                .statusCode(expectedStatus);
    }

    @Step("Получить список фильмов page={page}, pageSize={pageSize} (успех)")
    public FindAllMoviesResponse getMoviesSuccess(String token, int page, int pageSize) {
        return movieClient.getMovies(token, page, pageSize)
                .then()
                .statusCode(200)
                .extract()
                .as(FindAllMoviesResponse.class);
    }

    @Step("Обновить фильм id={id} (успех)")
    public MovieResponse patchMovieSuccess(String token, long id, Map<String, Object> patchBody) {
        return movieClient.patchMovie(token, id, patchBody)
                .then()
                .statusCode(200)
                .extract()
                .as(MovieResponse.class);
    }

    @Step("Обновить фильм id={id} (ожидаем статус {expectedStatus})")
    public void patchMovieError(String token, long id, Map<String, Object> patchBody, int expectedStatus) {
        movieClient.patchMovie(token, id, patchBody)
                .then()
                .statusCode(expectedStatus);
    }

    @Step("Удалить фильм id={id} (ожидаем статус {expectedStatus})")
    public void deleteMovie(String token, long id, int expectedStatus) {
        movieClient.deleteMovie(token, id)
                .then()
                .statusCode(expectedStatus);
    }

    @Step("Проверяем, что ответ API совпадает с запросом создания фильма")
    public void assertMovieMatchesCreateRequest(MovieResponse movie, CreateMovieRequest request) {
        assertThat(movie.getName()).isEqualTo(request.getName());
        assertThat(movie.getPrice()).isEqualTo(request.getPrice());
        assertThat(movie.getDescription()).isEqualTo(request.getDescription());
        assertThat(movie.getLocation()).isEqualTo(request.getLocation());
        assertThat(movie.getPublished()).isEqualTo(request.getPublished());
        assertThat(movie.getGenreId()).isEqualTo(request.getGenreId());

        assertThat(movie.getId()).isNotNull();
        assertThat(movie.getCreatedAt()).isNotNull();
        assertThat(movie.getRating()).isNotNull();
    }
}