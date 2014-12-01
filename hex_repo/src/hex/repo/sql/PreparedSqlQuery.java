package hex.repo.sql;

import hex.ql.ast.Literal;

/**
 * Created by jason on 14-12-01.
 */
public class PreparedSqlQuery extends SqlQuery {
    public PreparedSqlQuery(Metadata metadata) {
        super(metadata);
    }

    @Override
    protected StringBuilder renderLiteral(StringBuilder sql, Literal node) {
        return dialect.boundParam(sql);
    }
}
