package ca.thejayvm.hex.repo;

import ca.thejayvm.jill.Query;
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
    public static List LIST_ERROR = new ArrayList<>();

    private Predicate<T> predicate = new NullPredicate<>();

    @SuppressWarnings("unchecked")
    private List<T> list_error = (List<T>)LIST_ERROR;

    private List<Exception> exceptions = new ArrayList<>();

    protected abstract Metadata<T> get_metadata();

    public RepositoryBase<T> where(Predicate<T> predicate) {
        try {
            @SuppressWarnings("unchecked")
            RepositoryBase<T> q = (RepositoryBase<T>) clone();
            q.predicate = this.predicate.and(predicate);
            return q;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public List<T> toList() {
        String sql;
        try {
            sql = toSql();
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

    public String toSql() throws InvalidAstException {
        SqlQuery result = new SqlQuery(get_metadata());
        result.from(new Node[] { new Variable(getTableName()) });
        if(predicate != null && predicate instanceof Node) {
            result.where(((Node) predicate).toTree());
        }
        return result.toSql();
    }

    protected abstract String getTableName();
}
