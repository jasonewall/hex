package ca.thejayvm.hex.repo;

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

    private static SQLFunction<ResultSet,Object> findGetter(ResultSetMetaData meta, int columnIndex) throws SQLException {
        switch(meta.getColumnType(columnIndex)) {
            case Types.BIGINT: return (rs) -> rs.getLong(columnIndex);
            case Types.INTEGER: return (rs) -> rs.getInt(columnIndex);
            case Types.BOOLEAN: return (rs) -> rs.getBoolean(columnIndex);
            case Types.CHAR:
            case Types.LONGVARCHAR:
            case Types.VARCHAR:
                return (rs) -> rs.getString(columnIndex);
            case Types.DATE: return (rs) -> rs.getDate(columnIndex);
            case Types.DECIMAL:
            case Types.DOUBLE:
            case Types.FLOAT:
            case Types.NUMERIC:
                return (rs) -> rs.getDouble(columnIndex);
            case Types.LONGNVARCHAR:
            case Types.NCHAR:
            case Types.NVARCHAR:
                return (rs) -> rs.getNString(columnIndex);
            case Types.NULL: return (rs) -> null;
            case Types.SMALLINT: return (rs) -> rs.getShort(columnIndex);
            case Types.TIME:
            case Types.TIME_WITH_TIMEZONE:
                return (rs) -> rs.getTime(columnIndex);
            case Types.TIMESTAMP:
            case Types.TIMESTAMP_WITH_TIMEZONE:
                return (rs) -> rs.getTimestamp(columnIndex);
        }
        throw new SQLException("Unhandled column type found while mapping data to instance.");
    }

    public boolean next() throws SQLException {
        return rs.next();
    }

    public Object getValue(int colIndex) throws SQLException {
        return findGetter(metaData, colIndex).apply(rs);
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
