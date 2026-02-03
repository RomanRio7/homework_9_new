package cinescope.api.client;

import cinescope.api.dto.CreateMovieRequest;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static cinescope.api.spec.MoviesSpecs.authSpec;

public class MovieClient {

    private static final String BASE_PATH = "/movies";

    public Response createMovie(String token, CreateMovieRequest request) {
        return given()
                .spec(authSpec(token))
                .body(request)
                .when()
                .post(BASE_PATH);
    }

    public Response getMovie(String token, long id) {
        return given()
                .spec(authSpec(token))
                .when()
                .get(BASE_PATH + "/{id}", id);
    }

    public Response getMovies(String token, int page, int pageSize) {
        return given()
                .spec(authSpec(token))
                .queryParam("page", page)
                .queryParam("pageSize", pageSize)
                .when()
                .get(BASE_PATH);
    }

    public Response patchMovie(String token, long id, Object patchBody) {
        return given()
                .spec(authSpec(token))
                .body(patchBody)
                .when()
                .patch(BASE_PATH + "/{id}", id);
    }

    public Response deleteMovie(String token, long id) {
        return given()
                .spec(authSpec(token))
                .when()
                .delete(BASE_PATH + "/{id}", id);
    }
}