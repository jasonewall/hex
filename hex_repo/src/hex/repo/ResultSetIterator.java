package hex.repo;

import jill.ast.InvalidAstException;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

/**
 * Created by jason on 14-11-02.
 */
public class ResultSetIterator<T> implements Iterator<T> {
    private final RepositoryQuery<T> query;

    private Connection connection;

    private Statement stmt;

    private ResultSetWrapper resultSet;

    private boolean hasNext;

    public ResultSetIterator(RepositoryQuery<T> query) {
        this.query = query;
    }

    @Override
    public boolean hasNext() {
        return withResults((rs) -> hasNext);
    }

    @Override
    public T next() {
        return withResults((rs) -> {
            try {
                T result = query.getRepository().get_metadata().mapRecord(rs);
                hasNext = rs.next();
                return result;
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                close();
                query.getExceptions().add(e);
            }
            return null;
        });
    }

    private <R> R withResults(SQLFunction<ResultSetWrapper,R> consumer) {
        try {
            if(this.resultSet == null) {
                this.connection = ConnectionManager.getConnection();
                this.stmt = connection.createStatement();
                this.resultSet = new ResultSetWrapper(stmt.executeQuery(query.toSql()));
                this.hasNext = this.resultSet.next();
            }

            R result = consumer.apply(resultSet);
            if(!hasNext) {
                close();
            }
            return result;
        } catch (SQLException | InvalidAstException e) {
            this.query.getExceptions().add(e);
            close();
        }
        return null;
    }

    private void close() {
        closeQuietly(resultSet);
        closeQuietly(stmt);
        closeQuietly(connection);
    }

    private void closeQuietly(AutoCloseable closeable) {
        if(closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                this.query.getExceptions().add(e);
            }
        }
    }
}
