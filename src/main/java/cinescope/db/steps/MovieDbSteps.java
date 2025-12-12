package cinescope.db.steps;

import cinescope.db.dao.MoviesDao;
import cinescope.db.model.MovieDbModel;
import io.qameta.allure.Allure;

public class MovieDbSteps extends DbBaseSteps {

    public MovieDbSteps() {
        super();
    }

    public MovieDbModel getMovieById(long id) {
        Allure.step("Получаем фильм из БД по id=" + id);

        return jdbi.withExtension(MoviesDao.class,
                dao -> dao.findById(id));
    }

    public int getMoviesCountById(long id) {
        Allure.step("Считаем количество фильмов в БД по id=" + id);

        return jdbi.withExtension(MoviesDao.class,
                dao -> dao.countById(id));
    }
}