package hex.repo.sql;

import hex.ql.ast.*;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by jason on 14-10-26.
 */
public class SqlQuery {
    protected final Dialect dialect;

    private final Metadata metadata;

    private Node[] select = new Node[] { new Variable("*") };

    private boolean distinct;

    private Node[] from;

    private Node[] where;

    private long limit = -1;

    private long offset = -1;

    public SqlQuery(Metadata metadata) {
        this.metadata = metadata;
        this.dialect = new Dialect(){};
    }

    public SqlQuery select(Node[] select) {
        this.select = select;
        return this;
    }

    public SqlQuery distinct(boolean isDistinct) {
        this.distinct = isDistinct;
        return this;
    }

    public SqlQuery from(Node[] from) {
        this.from = from;
        return this;
    }

    public SqlQuery where(Node[] where) {
        this.where = where;
        return this;
    }

    public SqlQuery limit(long limit) {
        this.limit = limit;
        return this;
    }

    public SqlQuery offset(long offset) {
        this.offset = offset;
        return this;
    }

    public String toSql() throws InvalidAstException {
        StringBuilder sql = new StringBuilder();
        renderSelect(sql);
        dialect.separateClause(sql);
        renderFrom(sql);
        if(where != null) {
            dialect.separateClause(sql);
            renderWhere(sql);
        }
        if(limit > 0) {
            dialect.separateClause(sql);
            renderLimit(sql);
        }
        if(offset > 0) {
            dialect.separateClause(sql);
            renderOffset(sql);
        }
        return sql.toString();
    }

    private StringBuilder renderSelect(StringBuilder sql) throws InvalidAstException {
        dialect.select(sql);
        if(distinct) dialect.distinct(sql);
        return renderList(sql, select, (node) -> dialect.separateColumn(sql));
    }

    private StringBuilder renderFrom(StringBuilder sql) throws InvalidAstException {
        dialect.from(sql);
        return renderList(sql, from, (node) -> dialect.separateTable(sql));
    }

    private StringBuilder renderWhere(StringBuilder sql) throws InvalidAstException {
        dialect.where(sql);
        return renderTree(sql, where, (node) -> dialect.startParens(sql), (node) -> dialect.endParens(sql));
    }

    private StringBuilder renderLimit(StringBuilder sql) throws InvalidAstException {
        dialect.limit(sql);
        return renderNode(sql, new Literal<>(limit));
    }

    private StringBuilder renderOffset(StringBuilder sql) throws InvalidAstException {
        dialect.offset(sql);
        return renderNode(sql, new Literal<>(offset));
    }

    private StringBuilder renderNode(StringBuilder sql, Node node) throws InvalidAstException {
        if(node instanceof Variable) {
            return renderVariable(sql, (Variable)node);
        } else if(node instanceof PropertyRef) {
            return renderPropertyRef(sql, (PropertyRef)node);
        } else if(node instanceof Comparator) {
            return renderComparator(sql, (Comparator)node);
        } else if(node instanceof Literal) {
            return renderLiteral(sql, (Literal)node);
        } else if(node instanceof BooleanOperator) {
            return renderBooleanOperator(sql, (BooleanOperator)node);
        }
        throw new InvalidAstException("Unhandled node type found while generating SQL.");
    }

    private StringBuilder renderBooleanOperator(StringBuilder sql, BooleanOperator node) throws InvalidAstException {
        switch(node.getType()) {
            case And: return dialect.and(sql);
            case Or: return dialect.or(sql);
        }
        throw new InvalidAstException("Unhandled BooleanOperator type found while generating SQL.");
    }

    private StringBuilder renderComparator(StringBuilder sql, Comparator node) throws InvalidAstException {
        switch(node.getType()) {
            case Equals: return dialect.equalityComparison(sql);
            case NotEquals: return dialect.notEqualsComparison(sql);
        }
        throw new InvalidAstException("Unhandled comparator type found while generating SQL.");
    }

    protected StringBuilder renderLiteral(StringBuilder sql, Literal node) {
        Object value = node.getValue();
        if(value instanceof Number) {
            sql.append(value);
        } else {
            dialect.quote(sql, value);
        }
        return sql;
    }

    private StringBuilder renderVariable(StringBuilder sql, Variable node) {
        return dialect.escapeEntity(node.getName(), sql);
    }

    private StringBuilder renderPropertyRef(StringBuilder sql, PropertyRef node) {
        @SuppressWarnings("unchecked") Function<Object,Object> func = (Function<Object,Object>)node.getProperty();
        return dialect.escapeEntity(metadata.getFieldName(func), sql);
    }

    private StringBuilder renderList(StringBuilder sql, Node[] nodes, Consumer<Node> separateNode) throws InvalidAstException {
        return renderList(sql, Arrays.asList(nodes), separateNode);
    }

    private StringBuilder renderList(StringBuilder sql, List<Node> nodes, Consumer<Node> separateNode) throws InvalidAstException {
        Iterator<Node> it = nodes.iterator();
        renderNode(sql, it.next());
        while(it.hasNext()) {
            Node node = it.next();
            separateNode.accept(node);
            renderNode(sql, node);
        }
        return sql;
    }

    private StringBuilder renderTree(StringBuilder sql, Node[] nodes, Consumer<Node> begin, Consumer<Node> end) throws InvalidAstException {
        for(Node n : nodes) {
            Node[] subTree = n.toTree();
            if(subTree == null) {
                renderNode(sql, n);
            } else {
                begin.accept(n);
                renderTree(sql, subTree, begin, end);
                end.accept(n);
            }
        }

        return sql;
    }
}
