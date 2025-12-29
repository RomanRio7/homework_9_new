package cinescope.tests;

import cinescope.api.steps.AuthApiSteps;
import cinescope.api.steps.MovieApiSteps;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.List;

public abstract class ApiBaseTest {

    protected final AuthApiSteps authSteps = new AuthApiSteps();
    protected final MovieApiSteps movieSteps = new MovieApiSteps();

    protected String token;
    protected final List<Long> moviesToDelete = new ArrayList<>();

    @BeforeEach
    void setUp() {
        token = authSteps.loginAsAdmin();
    }

    @AfterEach
    void tearDown() {
        for (Long id : moviesToDelete) {
            try {
                movieSteps.deleteMovie(token, id, 200);
            } catch (Exception ignored) {
            }
        }
        moviesToDelete.clear();
    }
}