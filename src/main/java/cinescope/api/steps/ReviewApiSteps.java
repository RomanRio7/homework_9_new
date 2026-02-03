package cinescope.api.steps;

import cinescope.api.client.ReviewClient;
import cinescope.api.dto.CreateReviewRequest;
import io.qameta.allure.Step;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

public class ReviewApiSteps {

    private final ReviewClient reviewClient = new ReviewClient();

    @Step("Создать отзыв (успешно)")
    public void createReviewSuccess(String token, int movieId, CreateReviewRequest body) {
        reviewClient.createReview(token, movieId, body)
                .then()
                .statusCode(anyOf(is(200), is(201)));
    }

    @Step("Удалить отзыв (200/204/404)")
    public void deleteReviewOk(String token, int movieId, String userId) {
        reviewClient.deleteReview(token, movieId, userId)
                .then()
                .statusCode(anyOf(is(200), is(204), is(404)));
    }
}