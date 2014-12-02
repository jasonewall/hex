package hex.repo;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by jason on 14-12-01.
 */
public class PreparedStatementWrapper implements AutoCloseable {
    private final PreparedStatement stmt;

    private final List<Object> parameters;

    public PreparedStatementWrapper(PreparedStatement stmt, List<Object> parameters) {
        this.stmt = stmt;
        this.parameters = parameters;
    }

    public ResultSetWrapper executeQuery() throws SQLException {
        for (int i = 0; i < parameters.size(); i++) {
            setParam(i + 1, parameters.get(i));
        }
        return new ResultSetWrapper(stmt.executeQuery());
    }

    private void setParam(int parameterIndex, Object param) throws SQLException {
        if(param instanceof Integer)
            stmt.setInt(parameterIndex, (Integer)param);
        else if(param instanceof Long)
            stmt.setLong(parameterIndex, (Long)param);
        else if(param instanceof String)
            stmt.setString(parameterIndex, (String)param);
    }

    @Override
    public void close() throws SQLException {
        this.stmt.close();
    }
}
