package ca.thejayvm.hex.repo;

import ca.thejayvm.jill.Query;
import ca.thejayvm.jill.Queryable;
import ca.thejayvm.jill.ast.InvalidAstException;
import ca.thejayvm.jill.ast.Node;
import ca.thejayvm.jill.ast.Variable;
import ca.thejayvm.jill.sql.SqlQuery;

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

    private final Repository<T> repository;

    private Query<T> query = new Query<>();

    private Node[] where;

    private List<Exception> exceptions = new ArrayList<>();

    public RepositoryQuery(Repository<T> repository) {
        this.repository = repository;
    }

    public Repository<T> getRepository() {
        return repository;
    }

    @Override
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
    public RepositoryQuery<T> where(Predicate<T> predicate) {
        RepositoryQuery<T> q = duplicate();
        q.query = query.where(predicate);
        if(q.query.getPredicate() != null && q.query.getPredicate() instanceof Node) {
            try {
                q.where = ((Node) q.query.getPredicate()).toTree();
                return q;
            } catch (InvalidAstException e) {
                // ignored
            }
        }
        return null;
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
        dup.query = query;
        dup.where = where;
        return dup;
    }
}
