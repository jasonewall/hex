package hex.repo;

import hex.repo.sql.SqlQuerySuite;
import hex.repo.sql.SqlQueryTest;
import hex.repo.streams.RepositoryStreamTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        SqlQuerySuite.class,
        RepositoryStreamTest.class,
        RepositoryBaseTest.class
})
public class RepoTests {
}
