package ca.thejayvm.hex.repo;

import ca.thejayvm.jill.Query;
import ca.thejayvm.jill.Queryable;
import ca.thejayvm.jill.ast.*;
import ca.thejayvm.jill.sql.Metadata;
import ca.thejayvm.jill.sql.SqlQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by jason on 14-10-25.
 */
public abstract class RepositoryBase<T> implements Queryable<T> {
    protected Metadata metadata;

    public Query<T> where(Predicate<T> predicate) {
        return new Query<T>(this).where(predicate);
    }

    @Override
    public List<T> toList(Query<T> query) {
        return new ArrayList<>();
    }

    public String toSql(Query<T> query) throws InvalidAstException {
        SqlQuery result = new SqlQuery(metadata);
        result.from(new Node[] { new Variable(getTableName())});
        Predicate<T> predicate = query.getPredicate();
        if(predicate != null && predicate instanceof Node) {
            result.where(((Node) predicate).toTree());
        }
        return result.toSql();
    }

    protected abstract String getTableName();
}
