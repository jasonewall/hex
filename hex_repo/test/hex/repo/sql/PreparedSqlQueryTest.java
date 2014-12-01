package hex.repo.sql;

import hex.ql.ast.InvalidAstException;
import hex.repo.sql.test.Book;

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
}
