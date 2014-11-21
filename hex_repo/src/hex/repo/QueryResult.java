package hex.repo;

import hex.repo.metadata.DataMappingException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

/**
 * Created by jason on 14-11-02.
 */
public class QueryResult<T> implements Iterator<T>, AutoCloseable {
    private final AbstractRepository<T> repository;

    private Connection connection;

    private Statement stmt;

    private ResultSetWrapper resultSet;

    private boolean hasNext;

    private String sql;

    public QueryResult(AbstractRepository<T> repository, String sql) {
        this.repository = repository;
        this.sql = sql;
    }

    @Override
    public boolean hasNext() {
        return withResults((rs) -> hasNext);
    }

    @Override
    public T next() {
        return withResults((rs) -> {
            try {
                T result = repository.get_metadata().mapRecord(rs);
                hasNext = rs.next();
                return result;
            }  catch (DataMappingException e) {
                close();
                throw new RepositoryException(e.getCause());
            }
        });
    }

    private <R> R withResults(ResultSetMapper<R> mapper) {
        try {
            if(this.resultSet == null) {
                this.connection = ConnectionManager.getConnection();
                this.stmt = connection.createStatement();
                this.resultSet = new ResultSetWrapper(stmt.executeQuery(sql));
                this.hasNext = this.resultSet.next();
            }

            R result = mapper.apply(resultSet);
            if(!hasNext) {
                close();
            }
            return result;
        } catch (SQLException e) {
            close();
            throw new RepositoryException(e);
        } catch (RuntimeException | Error e) { // really ensure we call close if something bad happens
            close();
            throw e;
        }
    }

    public void close() {
        closeQuietly(resultSet);
        closeQuietly(stmt);
        closeQuietly(connection);
    }

    private void closeQuietly(AutoCloseable closeable) {
        if(closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                // ignored it
            }
        }
    }
}
