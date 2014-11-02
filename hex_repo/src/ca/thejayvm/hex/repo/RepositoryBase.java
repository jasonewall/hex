package ca.thejayvm.hex.repo;

import ca.thejayvm.jill.Queryable;
import ca.thejayvm.jill.ast.*;
import ca.thejayvm.jill.ast.predicates.NullPredicate;
import ca.thejayvm.jill.sql.SqlQuery;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Created by jason on 14-10-25.
 */
public abstract class RepositoryBase<T> implements Repository<T>, Queryable<T>, Cloneable {

    public abstract Metadata<T> get_metadata();

    public RepositoryQuery<T> where(Predicate<T> predicate) {
        return new RepositoryQuery<>(this).where(predicate);
    }

    @Override
    public List<T> toList() {
        return new RepositoryQuery<>(this).toList();
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
        return new RepositoryQuery<>(this).toSql();
    }

    public abstract String getTableName();
}
