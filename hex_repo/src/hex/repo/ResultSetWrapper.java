package hex.repo;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Created by jason on 14-11-01.
 */
public class ResultSetWrapper implements AutoCloseable {
    private final ResultSet rs;

    public ResultSetWrapper(ResultSet rs) {
        this.rs = rs;
    }

    public boolean next() throws SQLException {
        return rs.next();
    }

    public Object getValue(int columnIndex) throws SQLException {
        switch(getMetaData().getColumnType(columnIndex)) {
            case Types.BIGINT: return rs.getLong(columnIndex);
            case Types.INTEGER: return rs.getInt(columnIndex);
            case Types.BOOLEAN: return rs.getBoolean(columnIndex);
            case Types.CHAR:
            case Types.LONGVARCHAR:
            case Types.VARCHAR:
                return rs.getString(columnIndex);
            case Types.DATE: return rs.getDate(columnIndex);
            case Types.DECIMAL:
            case Types.DOUBLE:
            case Types.FLOAT:
            case Types.NUMERIC:
                return rs.getDouble(columnIndex);
            case Types.LONGNVARCHAR:
            case Types.NCHAR:
            case Types.NVARCHAR:
                return rs.getNString(columnIndex);
            case Types.NULL: return null;
            case Types.SMALLINT: return rs.getShort(columnIndex);
            case Types.TIME:
            case Types.TIME_WITH_TIMEZONE:
                return rs.getTime(columnIndex);
            case Types.TIMESTAMP:
            case Types.TIMESTAMP_WITH_TIMEZONE:
                return rs.getTimestamp(columnIndex);
        }
        throw new SQLException("Unhandled column type found while mapping data to instance.");
    }

    private ResultSetMetaData metaData;
    public ResultSetMetaData getMetaData() throws SQLException {
        if(metaData == null) {
            this.metaData = rs.getMetaData();
        }

        return this.metaData;
    }

    @Override
    public void close() throws SQLException {
        this.rs.close();
    }
}
