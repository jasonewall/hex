package hex.repo;

import hex.repo.sql.SqlQuerySuite;
import hex.repo.sql.SqlQueryTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({SqlQuerySuite.class})
public class RepoTests {
}
