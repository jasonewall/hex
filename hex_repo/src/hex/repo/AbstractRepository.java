package hex.repo;

import hex.repo.metadata.Metadata;
import hex.repo.streams.RepositoryStream;
import hex.ql.Queryable;
import hex.ql.ast.InvalidAstException;
import hex.ql.ast.predicates.EqualityPredicate;

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

    @Override
    public Iterator<T> iterator() {
        return stream().iterator();
    }

    public String toSql() throws InvalidAstException {
        return stream().toSql();
    }

    public abstract String getTableName();
}
