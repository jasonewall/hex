package ca.thejayvm.hex.repo;

import ca.thejayvm.jill.Query;
import ca.thejayvm.jill.Queryable;

import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Created by jason on 14-11-01.
 */
public interface Repository<T> extends Queryable<T> {
    public Queryable<T> where(Predicate<T> predicate);

    Metadata<T> get_metadata();

    String getTableName();

    public void execute_query(String query, Consumer<ResultSetWrapper> consumer) throws SQLException;
}
