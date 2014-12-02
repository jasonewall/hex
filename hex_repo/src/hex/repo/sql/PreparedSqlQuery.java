package hex.repo.sql;

import hex.ql.ast.Literal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jason on 14-12-01.
 */
public class PreparedSqlQuery extends SqlQuery {
    private List<Object> parameterValues = new ArrayList<>();

    public PreparedSqlQuery(Metadata metadata) {
        super(metadata);
    }

    @Override
    protected StringBuilder renderLiteral(StringBuilder sql, Literal node) {
        parameterValues.add(node.getValue());
        return dialect.boundParam(sql);
    }

    public List<Object> getParameterValues() {
        return parameterValues;
    }
}
