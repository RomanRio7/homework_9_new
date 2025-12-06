package cinescope.db.steps;

import cinescope.db.dao.MoviesDao;
import cinescope.db.model.MovieDbModel;

import static org.assertj.core.api.Assertions.assertThat;

public class MovieDbSteps extends DbBaseSteps {

    public MovieDbSteps() {
        super();
    }

    public void assertMovieExists(long id) {
        MovieDbModel movie = jdbi.withExtension(MoviesDao.class,
                dao -> dao.findById(id));

        assertThat(movie)
                .as("Ожидаем, что фильм с id=%s существует в БД", id)
                .isNotNull();
    }

    public void assertMovieNotExists(long id) {
        int count = jdbi.withExtension(MoviesDao.class,
                dao -> dao.countById(id));

        assertThat(count)
                .as("Ожидаем, что фильм с id=%s отсутствует в БД", id)
                .isZero();
    }

    public void assertUpdatedField(long id, String fieldName, Object expectedValue) {
        MovieDbModel movie = jdbi.withExtension(MoviesDao.class,
                dao -> dao.findById(id));

        assertThat(movie)
                .as("Фильм с id=%s должен существовать в БД для проверки поля %s", id, fieldName)
                .isNotNull();

        switch (fieldName) {
            case "name" -> assertThat(movie.getName()).isEqualTo(expectedValue);
            case "price" -> assertThat(movie.getPrice()).isEqualTo(expectedValue);
            case "description" -> assertThat(movie.getDescription()).isEqualTo(expectedValue);
            case "location" -> assertThat(movie.getLocation()).isEqualTo(expectedValue);
            case "published" -> assertThat(movie.getPublished()).isEqualTo(expectedValue);
            case "genreId" -> assertThat(movie.getGenreId()).isEqualTo(expectedValue);
            default -> throw new IllegalArgumentException("Неизвестное поле: " + fieldName);
        }
    }
}