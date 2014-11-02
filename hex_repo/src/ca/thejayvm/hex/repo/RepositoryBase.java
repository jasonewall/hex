package ca.thejayvm.hex.repo;

import ca.thejayvm.jill.Query;
import ca.thejayvm.jill.Queryable;
import ca.thejayvm.jill.ast.*;
import ca.thejayvm.jill.sql.SqlQuery;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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
        try {
            try (
                    Connection conn = ConnectionManager.getConnection();
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(toSql(query))
            ) {
                ResultSetMetaData rs_meta = rs.getMetaData();
                int col_count = rs_meta.getColumnCount();

                List<T> results = new ArrayList<>();

                while(rs.next()) {
                    T record = get_metadata().newInstance();
                    for(int i = 1; i <= col_count; i++) {
                        Method setter = get_metadata().getSetter(rs_meta.getColumnLabel(i));
                        setter.invoke(record, Metadata.findGetter(rs_meta, i).apply(rs));
                    }
                    results.add(record);
                }

                return results;
            }
        } catch(SQLException|InvalidAstException|IllegalAccessException|InvocationTargetException|InstantiationException
                |NoSuchMethodException e) {
            exceptions.add(e);
        }
        return list_error;
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
