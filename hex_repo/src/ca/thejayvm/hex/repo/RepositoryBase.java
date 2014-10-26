package ca.thejayvm.hex.repo;

import ca.thejayvm.jill.Query;
import ca.thejayvm.jill.Queryable;
import ca.thejayvm.jill.ast.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Created by jason on 14-10-25.
 */
public abstract class RepositoryBase<T> implements Queryable<T> {
    public Query<T> where(Predicate<T> predicate) {
        return new Query<T>(this).where(predicate);
    }

    @Override
    public List<T> toList(Query<T> query) {
        return new ArrayList<>();
    }

    public String toSql(Query<T> query) throws InvalidAstException {
        StringBuilder builder = new StringBuilder();

        Predicate<T> predicate = query.getPredicate();
        if(predicate instanceof Node) {
            Node[] ast = ((Node)predicate).toTree();
            builder.append("WHERE ");
            for(Node n : ast) {
                builder.append(toSql(n));
            }
        }

        return builder.toString();
    }

    private String toSql(Node n) throws InvalidAstException {
        if(n instanceof PropertyRef) {
            @SuppressWarnings("unchecked") PropertyRef<T,Object> ref = (PropertyRef)n;
            Object key = ref.getProperty().apply(this.keyRecord);
            return keyFields.get(key);
        } else if(n instanceof Literal) {
            Object value = ((Literal)n).getValue();
            if(value instanceof Number)
                return value.toString();
            else
                return String.format("'%s'", value.toString());
        } else if(n instanceof Comparator) {
            Comparator op = (Comparator)n;
            switch(op.getType()) {
                case Equals: return " = ";
                default: throw new InvalidAstException("Unhandled operator found while generating SQL");
            }
        }

        throw new InvalidAstException("Unknown node type while generating SQL");
    }

    protected Map<Object,String> keyFields = new HashMap<>();

    protected T keyRecord;
}
