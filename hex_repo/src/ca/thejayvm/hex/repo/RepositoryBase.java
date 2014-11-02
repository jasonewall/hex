package ca.thejayvm.hex.repo;

import ca.thejayvm.jill.Query;
import ca.thejayvm.jill.Queryable;
import ca.thejayvm.jill.ast.*;
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
public abstract class RepositoryBase<T> implements Queryable<T> {
    public static List LIST_ERROR = new ArrayList<>();

    @SuppressWarnings("unchecked")
    private List<T> list_error = (List<T>)LIST_ERROR;

    private List<Exception> exceptions = new ArrayList<>();

    protected abstract Metadata<T> get_metadata();

    public Query<T> where(Predicate<T> predicate) {
        return new Query<T>(this).where(predicate);
    }

    @Override
    public List<T> toList(Query<T> query) {
        String sql;
        try {
            sql = toSql(query);
        } catch (InvalidAstException e) {
            this.exceptions.add(e);
            return list_error;
        }

        try {
            List<T> results = new ArrayList<>();
            executeQuery(sql, (rs) -> {
                try {
                    while(rs.next()) {
                        results.add(get_metadata().mapRecord(rs));
                    }
                } catch (SQLException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
                    this.exceptions.add(e);
                }
            });
            if(this.exceptions.isEmpty()) return results;
        } catch (SQLException e) {
            this.exceptions.add(e);
        }

        return list_error;
    }

    public void executeQuery(String query, Consumer<ResultSetWrapper> consumer) throws SQLException {
        try (
                Connection conn = ConnectionManager.getConnection();
                Statement stmt = conn.createStatement();
                ResultSetWrapper rs = new ResultSetWrapper(stmt.executeQuery(query))
        ) {
            consumer.accept(rs);
        }
    }

    public List<Exception> getExceptions() {
        return exceptions;
    }

    public String toSql(Query<T> query) throws InvalidAstException {
        SqlQuery result = new SqlQuery(get_metadata());
        result.from(new Node[] { new Variable(getTableName()) });
        Predicate<T> predicate = query.getPredicate();
        if(predicate != null && predicate instanceof Node) {
            result.where(((Node) predicate).toTree());
        }
        return result.toSql();
    }

    protected abstract String getTableName();
}
