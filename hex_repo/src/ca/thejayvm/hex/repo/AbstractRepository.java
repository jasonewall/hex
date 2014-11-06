package ca.thejayvm.hex.repo;

import ca.thejayvm.jill.Queryable;
import ca.thejayvm.jill.ast.InvalidAstException;
import ca.thejayvm.jill.ast.predicates.Condition;
import ca.thejayvm.jill.ast.predicates.EqualityPredicate;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Created by jason on 14-11-05.
 */
public abstract class AbstractRepository<T> implements Repository<T>, Queryable<T> {
    private T record_not_found = null;

    public abstract Metadata<T> get_metadata();

    public Queryable<T> where(Predicate<T> predicate) {
        return query().where(predicate);
    }

    public T find(int id) {
        List<T> query = query().where(new Condition<>(get_metadata().getPrimaryKey(), new EqualityPredicate<>(id))).toList();
        if(query.isEmpty()) return record_not_found;
        return query.get(0);
    }

    @Override
    public List<T> toList() {
        return query().toList();
    }

    private RepositoryQuery<T> query() {
        return new RepositoryQuery<>(this);
    }

    @Override
    public Iterator<T> iterator() {
        return query().iterator();
    }

    public void execute_query(String query, Consumer<ResultSetWrapper> consumer) throws SQLException {
        try (
                Connection conn = ConnectionManager.getConnection();
                Statement stmt = conn.createStatement();
                ResultSetWrapper rs = new ResultSetWrapper(stmt.executeQuery(query))
        ) {
            consumer.accept(rs);
        }
    }

    public String toSql() throws InvalidAstException {
        return query().toSql();
    }

    public abstract String getTableName();
}
