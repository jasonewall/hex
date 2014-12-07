package hex.repo;

import hex.repo.test.Person;
import hex.repo.test.PersonRepository;
import junit.framework.AssertionFailedError;
import org.junit.Test;

import static org.junit.Assert.*;

import static hex.ql.QueryLanguage.*;

/**
 * Created by jason on 14-12-04.
 */
public class RepositoryBaseTest {
    @Test
    public void findShouldReturnAValue() {
        Repository<Person> repo = new PersonRepository();
        Person fig = repo.stream().filter(
                where(Person::getFirstName, is("Fig"))
                .and(where(Person::getLastName, is("Newton")))
        ).findFirst()
                .orElseThrow(AssertionFailedError::new);

        Person findResults = repo.find(fig.getId()).get();
        assertNotNull(findResults);
        assertEquals("Fig", findResults.getFirstName());
        assertEquals("Newton", findResults.getLastName());
    }
}
