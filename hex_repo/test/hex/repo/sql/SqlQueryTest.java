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
        SqlQuery q = new SqlQuery(Book.metadata());
        q.from(new Node[]{ new Variable("books") });

        @SuppressWarnings("unchecked")
        Condition<Book,String> condition = (Condition<Book,String>) where(Book::getTitle, is("1984"));
        q.where(condition.toTree());

        String expected = "SELECT * FROM books WHERE title = '1984'";
        assertEquals(expected, q.toSql());
    }

    @Test
    public void testSelectWithColumns() throws InvalidAstException {
        SqlQuery q = new SqlQuery(null);

        q.select(new Node[] { new Variable("id"), new Variable("first_name"), new Variable("last_name") });
        q.from(new Node[]{ new Variable("employees") });

        String expected = "SELECT id, first_name, last_name FROM employees";
        assertEquals(expected, q.toSql());
    }

    @Test
    public void testCompoundCondition() throws InvalidAstException {
        Node condition = (Node) where(Book::getTitle, is("1984")).and(where(Book::getPublishedYear, is(1984)));
        SqlQuery q = new SqlQuery(Book.metadata());
        q.from(new Node[]{ new Variable("books") });
        q.where(condition.toTree());
        String expected = "SELECT * FROM books WHERE (title = '1984') AND (published_year = 1984)";
        assertEquals(expected, q.toSql());
    }
}
