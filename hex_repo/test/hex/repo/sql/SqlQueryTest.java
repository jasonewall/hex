package hex.repo.sql;

import hex.ql.ast.InvalidAstException;
import hex.ql.ast.Node;
import hex.ql.ast.Variable;
import hex.ql.ast.predicates.Condition;
import hex.repo.sql.test.Book;
import org.junit.Test;

import static org.junit.Assert.*;
import static hex.ql.QueryLanguage.*;

/**
 * Created by jason on 14-10-26.
 */
public class SqlQueryTest {
    @Test
    public void testSelectStarNoWhere() throws InvalidAstException {
        SqlQuery q = new SqlQuery(null);
        q.from(new Node[]{ new Variable("people") });
        String expected = "SELECT * FROM people";
        assertEquals(expected, q.toSql());
    }

    @Test
    public void testSelectStarWithWhere() throws InvalidAstException {
        SqlQuery q = getSelectStarWithWhere();

        String expected = "SELECT * FROM books WHERE title = '1984'";
        assertEquals(expected, q.toSql());
    }

    @Test
    public void testSelectWithColumns() throws InvalidAstException {
        SqlQuery q = new SqlQuery(null);

        q.select(new Node[]{new Variable("id"), new Variable("first_name"), new Variable("last_name")});
        q.from(new Node[]{ new Variable("employees") });

        String expected = "SELECT id, first_name, last_name FROM employees";
        assertEquals(expected, q.toSql());
    }

    @Test
    public void testCompoundCondition() throws InvalidAstException {
        SqlQuery q = getCompoundWhere();
        String expected = "SELECT * FROM books WHERE (title = '1984') AND (published_year = 1984)";
        assertEquals(expected, q.toSql());
    }

    @Test
    public void shouldUseDistinctKeywordWhenSet() throws InvalidAstException {
        SqlQuery q = getSqlQuery();
        q.select(new Node[] { new Variable("title") });
        q.distinct(true);
        q.from(new Node[]{ new Variable("books") });

        String expected = "SELECT DISTINCT title FROM books";
        assertEquals(expected, q.toSql());
    }

    @Test
    public void shouldRespectLimit() throws InvalidAstException {
        SqlQuery q = getBooksQuery();
        q.limit(10);
        String expected = "SELECT id, title, published_year FROM books LIMIT 10";
        assertEquals(expected, q.toSql());
    }

    @Test
    public void shouldRespectOffset() throws InvalidAstException {
        SqlQuery q = getBooksQuery();
        q.offset(3);
        String expected = "SELECT id, title, published_year FROM books OFFSET 3";
        assertEquals(expected, q.toSql());
    }

    protected SqlQuery getCompoundWhere() throws InvalidAstException {
        Node condition = (Node) where(Book::getTitle, is("1984")).and(where(Book::getPublishedYear, is(1984)));
        SqlQuery q = getSqlQuery();
        q.from(new Node[]{ new Variable("books") });
        q.where(condition.toTree());
        return q;
    }

    protected SqlQuery getSelectStarWithWhere() throws InvalidAstException {
        SqlQuery q = getSqlQuery();
        q.from(new Node[]{ new Variable("books") });

        Condition<Book,String> condition = where(Book::getTitle, is("1984"));
        q.where(condition.toTree());
        return q;
    }

    protected SqlQuery getBooksQuery() {
        SqlQuery q = getSqlQuery();
        q.select(new Node[]{ new Variable("id"), new Variable("title"), new Variable("published_year") });
        q.from(new Node[]{ new Variable("books")});
        return q;
    }

    protected SqlQuery getSqlQuery() {
        return new SqlQuery(Book.metadata());
    }
}
