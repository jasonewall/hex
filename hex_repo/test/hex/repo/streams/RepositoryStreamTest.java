package hex.repo.streams;

import hex.repo.test.Person;
import hex.repo.test.PersonRepository;
import org.junit.Test;

import static hex.ql.QueryLanguage.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by jason on 14-12-02.
 */
public class RepositoryStreamTest {
    @Test
    public void limitShouldBeRespected() {
        PersonRepository repo = new PersonRepository();
        Person[] aPerson = repo.stream().where(Person::getLastName, is("Newton")).limit(1).toArray(Person[]::new);
        assertEquals(1, aPerson.length);
    }
}
