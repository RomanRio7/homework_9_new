package cinescope.api.steps;

import cinescope.api.client.MovieClient;
import cinescope.api.dto.CreateMovieRequest;
import cinescope.api.dto.FindAllMoviesResponse;
import cinescope.api.dto.MovieResponse;
import io.restassured.response.Response;

import static org.assertj.core.api.Assertions.assertThat;

public class MovieApiSteps {

    private final MovieClient movieClient = new MovieClient();

    public MovieResponse createMovie(String token, CreateMovieRequest request, int expectedStatus) {
        Response response = movieClient.createMovie(token, request);

        response.then().statusCode(expectedStatus);

        if (expectedStatus != 201) {
            // негативные сценарии — просто проверили статус и вышли
            return null;
        }

        MovieResponse movie = response.as(MovieResponse.class);
        assertMovieMatchesCreateRequest(movie, request);
        return movie;
    }

    public MovieResponse getMovie(String token, long id, int expectedStatus) {
        Response response = movieClient.getMovie(token, id);

        response.then().statusCode(expectedStatus);

        if (expectedStatus != 200) {
            return null;
        }

        return response.as(MovieResponse.class);
    }

    public MovieResponse patchMovie(String token, long id, Object patchBody, int expectedStatus) {
        Response response = movieClient.patchMovie(token, id, patchBody);

        response.then().statusCode(expectedStatus);

        if (expectedStatus != 200) {
            return null;
        }

        return response.as(MovieResponse.class);
    }

    public void deleteMovie(String token, long id, int expectedStatus) {
        movieClient
                .deleteMovie(token, id)
                .then()
                .statusCode(expectedStatus);
    }

    public FindAllMoviesResponse getMovies(String token, int page, int pageSize, int expectedStatus) {
        Response response = movieClient.getMovies(token, page, pageSize);

        response.then().statusCode(expectedStatus);

        if (expectedStatus != 200) {
            return null;
        }

        return response.as(FindAllMoviesResponse.class);
    }

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