package hex.repo;

import hex.repo.metadata.Metadata;
import hex.repo.streams.RepositoryStream;
import hex.ql.Queryable;
import hex.ql.ast.predicates.EqualityPredicate;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Optional;

/**
 * Created by jason on 14-11-05.
 */
public abstract class AbstractRepository<T> implements Repository<T>, Queryable<T> {
    public abstract Metadata<T> get_metadata();

    public Optional<T> find(int id) {
        return where(get_metadata().getPrimaryKey(), new EqualityPredicate<>(id)).findFirst();
    }

    public RepositoryStream<T> stream() {
        return new RepositoryStream<>(this);
    }

    public <R> R executeQuery(String sql, ResultSetMapper<R> mapper) {
        try(Connection conn = ConnectionManager.getConnection();
            Statement stmt = conn.createStatement();
            ResultSetWrapper rs = new ResultSetWrapper(stmt.executeQuery(sql))) {
            return mapper.apply(rs);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Iterator<T> iterator() {
        return stream().iterator();
    }

    public abstract String getTableName();
}
