package cinescope.tests;

import cinescope.api.dto.CreateReviewRequest;
import cinescope.api.steps.AuthApiSteps;
import cinescope.api.steps.ReviewApiSteps;
import org.junit.jupiter.api.Test;

public class AddReviewUiTest {

    private final AuthApiSteps authSteps = new AuthApiSteps();
    private final ReviewApiSteps reviewApiSteps = new ReviewApiSteps();

    @Test
    void createAndDeleteReviewByApi() {
        String token = authSteps.loginAsAdmin();

        String userId = authSteps.getUserIdFromToken(token);

        int movieId = 53;

        CreateReviewRequest body = new CreateReviewRequest(
                "Отзыв " + System.currentTimeMillis(),
                5,
                false
        );
        reviewApiSteps.createReviewSuccess(token, movieId, body);

        reviewApiSteps.deleteReviewOk(token, movieId, userId);
    }
}