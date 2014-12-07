package hex.repo.streams;

import hex.ql.ast.InvalidAstException;
import hex.repo.test.Person;
import hex.repo.test.PersonRepository;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static hex.ql.QueryLanguage.*;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

/**
 * Created by jason on 14-12-02.
 */
public class RepositoryStreamTest {
    @Test
    public void limitShouldBeRespected() {
        PersonRepository repo = new PersonRepository();
        Person[] aPerson = repo.stream().where(Person::getLastName, is("Newton")).limit(1).toArray(Person[]::new);
        assertThat(aPerson.length, equalTo(1));
    }

    @Test
    public void offsetShouldBeRespected() throws InvalidAstException {
        PersonRepository repo = new PersonRepository();
        repo.stream().where(Person::getLastName, is("Newton")).skip(3)
                .forEach(p -> fail("Did not expect " + p.getFirstName()));
    }

    @Test
    public void peekShouldWork() {
        StringBuilder tester = new StringBuilder();
        PersonRepository repo = new PersonRepository();
        repo.stream().where(Person::getFirstName, is("Fig")).peek(p -> tester.append(p.getLastName())).toArray();
        assertThat("Newton", equalTo(tester.toString()));
    }

    @Test
    public void notEqualsShouldFilter() {
        PersonRepository repo = new PersonRepository();
        repo.stream().filter(
                where(Person::getLastName, isNot("Newton"))
                    .and(where(Person::getLastName, is("Newton")))
        ).forEach(p -> fail(p.getFullName()));
    }

    @Test
    public void complicatedQueriesShouldWork() {
        PersonRepository repo = new PersonRepository();
        List<Person> people = repo.familyMembers("Newton", "Wayne", "Isaac").parallel().collect(Collectors.toList());
        assertThat(2, equalTo(people.size()));
    }

    @Test
    public void complicatedQueriesShouldWorkEvenIfTheReturnNoResults() {
        PersonRepository repo = new PersonRepository();
        repo.familyMembers("Newton", "Wayne", "Isaac").where(Person::getFirstName, is("Fig"))
                .forEach(p -> fail("Did not expect " + p.getFirstName()));
    }

    @Test
    public void breakingOutOfTheDatabaseShouldBeSeamless() {
        PersonRepository repo = new PersonRepository();
        repo.familyMembers("Newton", "Fig")
                .filter(p -> p.getFirstName().equals("Wayne"))
                .forEach(p -> fail("Did not expect " + p.getFirstName()));
    }

    @Test
    public void testPeekSql() throws InvalidAstException {
        PersonRepository repo = new PersonRepository();
        //noinspection MismatchedQueryAndUpdateOfStringBuilder
        StringBuilder sql = new StringBuilder();
        RepositoryStream<Person> stream = (RepositoryStream<Person>) repo.familyMembers("Newton", "Fig", "Wayne");
        stream.peekSql(sql::append);
        List<Person> people = stream.collect(Collectors.toList());
        assertThat(people.size(), equalTo(2));
        assertThat(sql.toString(), containsString("FROM people"));
    }
}
