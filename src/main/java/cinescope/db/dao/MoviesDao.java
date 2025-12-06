package cinescope.db.dao;

import cinescope.db.model.MovieDbModel;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

public interface MoviesDao {

    @SqlQuery("SELECT * FROM movies WHERE id = :id")
    @RegisterBeanMapper(MovieDbModel.class)
    MovieDbModel findById(@Bind("id") long id);

    @SqlQuery("SELECT COUNT(*) FROM movies WHERE id = :id")
    int countById(@Bind("id") long id);
}
