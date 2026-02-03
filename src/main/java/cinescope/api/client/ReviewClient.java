package cinescope.api.client;

import cinescope.api.dto.CreateReviewRequest;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static cinescope.api.spec.MoviesSpecs.authSpec;

public class ReviewClient {

    public Response createReview(String token, int movieId, CreateReviewRequest body) {
        return given()
                .spec(authSpec(token))
                .body(body)
                .when()
                .post("/movies/{movieId}/reviews", movieId);
    }

    public Response deleteReview(String token, int movieId, String userId) {
        return given()
                .spec(authSpec(token))
                .queryParam("userId", userId)
                .when()
                .delete("/movies/{movieId}/reviews", movieId);
    }
}