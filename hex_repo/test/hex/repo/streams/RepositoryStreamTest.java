package hex.repo.streams;

import hex.repo.test.Person;
import hex.repo.test.PersonRepository;
import org.junit.Test;

import static hex.ql.QueryLanguage.*;
import static org.junit.Assert.*;

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

    @Test
    public void peekShouldWork() {
        StringBuilder tester = new StringBuilder();
        PersonRepository repo = new PersonRepository();
        repo.stream().where(Person::getFirstName, is("Fig")).peek(p -> tester.append(p.getLastName())).toArray();
        assertEquals("Newton", tester.toString());
    }

    @Test
    public void notEqualsShouldFilter() {
        PersonRepository repo = new PersonRepository();
        repo.stream().filter(
                where(Person::getLastName, isNot("Newton"))
                    .and(where(Person::getLastName, is("Newton")))
        ).forEach(p -> fail(p.getFullName()));
    }
}
