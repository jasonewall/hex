package hex.repo;

import jill.Queryable;
import jill.ast.InvalidAstException;
import jill.ast.Node;
import jill.ast.Variable;
import jill.ast.predicates.NullPredicate;
import jill.collections.CollectionQuery;
import jill.sql.SqlQuery;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by jason on 14-11-02.
 */
public class RepositoryQuery<T> implements Queryable<T> {
    public static List LIST_ERROR = new ArrayList<>();

    @SuppressWarnings("unchecked")
    private List<T> list_error = (List<T>)LIST_ERROR;

    private final AbstractRepository<T> repository;

    private Node[] where;

    private Predicate<T> predicate = new NullPredicate<>();

    private List<Exception> exceptions = new ArrayList<>();

    public RepositoryQuery(AbstractRepository<T> repository) {
        this.repository = repository;
    }

    public AbstractRepository<T> getRepository() {
        return repository;
    }

    public List<T> toList() {
        String sql;
        try {
            sql = toSql();
        } catch (InvalidAstException e) {
            this.exceptions.add(e);
            return list_error;
        }

        try {
            List<T> results = new ArrayList<>();
            repository.execute_query(sql, (rs) -> {
                try {
                    while(rs.next()) {
                        results.add(repository.get_metadata().mapRecord(rs));
                    }
                } catch (SQLException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    exceptions.add(e);
                }
            });

            if(exceptions.isEmpty()) return results;
        } catch (SQLException e) {
            exceptions.add(e);
        }

        return list_error;
    }

    @Override
    public Queryable<T> where(Predicate<T> predicate) {
        RepositoryQuery<T> q = duplicate();
        q.predicate = this.predicate.and(predicate);
        if(q.predicate instanceof Node) {
            try {
                q.where = ((Node) q.predicate).toTree();
                return q;
            } catch (InvalidAstException e) {
                // ignored
            }
        }
        return new CollectionQuery<T>(this).where(predicate);
    }

    public Iterator<T> iterator() {
        return new ResultSetIterator<>(this);
    }

    public String toSql() throws InvalidAstException {
        SqlQuery result = new SqlQuery(repository.get_metadata());
        result.from(new Node[]{new Variable(repository.getTableName())});
        result.where(where);
        return result.toSql();
    }

    public List<Exception> getExceptions() {
        return exceptions;
    }

    private RepositoryQuery<T> duplicate() {
        RepositoryQuery<T> dup = new RepositoryQuery<>(this.repository);
        dup.predicate = predicate;
        dup.where = where;
        return dup;
    }
}
