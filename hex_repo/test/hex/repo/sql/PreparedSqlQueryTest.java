package hex.repo.sql;

import hex.ql.ast.InvalidAstException;
import hex.repo.sql.test.Book;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by jason on 14-12-01.
 */
public class PreparedSqlQueryTest extends SqlQueryTest {
    @Override
    protected SqlQuery getSqlQuery() {
        return new PreparedSqlQuery(Book.metadata());
    }

    @Override
    public void testSelectStarWithWhere() throws InvalidAstException {
        SqlQuery q = getSelectStarWithWhere();
        String expected = "SELECT * FROM books WHERE title = ?";
        assertEquals(expected, q.toSql());
    }

    @Override
    public void testCompoundCondition() throws InvalidAstException {
        SqlQuery q = getCompoundWhere();
        String expected = "SELECT * FROM books WHERE (title = ?) AND (published_year = ?)";
        assertEquals(expected, q.toSql());
    }

    @Override
    public void shouldRespectLimit() throws InvalidAstException {
        PreparedSqlQuery q = (PreparedSqlQuery)getBooksQuery();
        q.limit(10);
        String expected = "SELECT id, title, published_year FROM books LIMIT ?";
        assertEquals(expected, q.toSql());
        assertEquals("Should have parameter value", 10L, q.getParameterValues().get(0));
    }

    @Test
    public void shouldTrackParametersItBinds() throws InvalidAstException {
        PreparedSqlQuery q = (PreparedSqlQuery) getCompoundWhere();
        q.toSql(); // to gather params
        List<Object> parameterValues = q.getParameterValues();
        assertEquals("Should be 2 bound parameters", 2, parameterValues.size());
        assertEquals("Param 1", parameterValues.get(0), "1984");
        assertEquals("Param 2", parameterValues.get(1), 1984);
    }
}
