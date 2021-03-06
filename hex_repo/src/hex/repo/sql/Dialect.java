package hex.repo.sql;

/**
 * Created by jason on 14-10-26.
 */
public interface Dialect {
    static final char SPACE = ' ';

    static final String SELECT = "SELECT ";
    default public StringBuilder select(StringBuilder sql) {
        return sql.append(SELECT);
    }

    static final String DISTINCT = "DISTINCT ";
    default public StringBuilder distinct(StringBuilder sql) {
        return sql.append(DISTINCT);
    }

    static final String FROM = "FROM ";
    default public StringBuilder from(StringBuilder sql) {
        return sql.append(FROM);
    }

    static final String WHERE = "WHERE ";
    default public StringBuilder where(StringBuilder sql) {
        return sql.append(WHERE);
    }

    static final String COLUMN_SEPARATOR = ", ";
    default public StringBuilder separateColumn(StringBuilder sql) {
        return sql.append(COLUMN_SEPARATOR);
    }

    static final String TABLE_SEPARATOR = ", ";
    default public StringBuilder separateTable(StringBuilder sql) {
        return sql.append(TABLE_SEPARATOR);
    }

    default public StringBuilder escapeEntity(Object name, StringBuilder sql) {
        return sql.append(name);
    }

    default public StringBuilder separateClause(StringBuilder sql) {
        return sql.append(SPACE);
    }

    static final String EQUALS = " = ";
    default public StringBuilder equalityComparison(StringBuilder sql) {
        return sql.append(EQUALS);
    }

    static final String NOT_EQUALS = " <> ";
    default public StringBuilder notEqualsComparison(StringBuilder sql) {
        return sql.append(NOT_EQUALS);
    }

    static final char QUOTE = '\'';
    default public StringBuilder quote(StringBuilder sql, Object value) {
        return sql.append(QUOTE).append(value).append(QUOTE);
    }

    static final char OPEN_PAREN = '(';
    static final char CLOSE_PAREN = ')';

    default public StringBuilder startParens(StringBuilder sql) {
        return sql.append(OPEN_PAREN);
    }

    default public StringBuilder endParens(StringBuilder sql) {
        return sql.append(CLOSE_PAREN);
    }

    static final String AND = " AND ";
    default public StringBuilder and(StringBuilder sql) {
        return sql.append(AND);
    }

    static final String OR = " OR ";
    default public StringBuilder or(StringBuilder sql) {
        return sql.append(OR);
    }

    static final String BOUND_PARAM = "?";
    default public StringBuilder boundParam(StringBuilder sql) {
        return sql.append(BOUND_PARAM);
    }

    static final String LIMIT = "LIMIT ";
    default public StringBuilder limit(StringBuilder sql) {
        return sql.append(LIMIT);
    }

    static final String OFFSET = "OFFSET ";
    default public StringBuilder offset(StringBuilder sql) {
        return sql.append(OFFSET);
    }
}
