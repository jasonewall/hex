package hex.repo.test;

import hex.repo.Repository;
import hex.repo.streams.RepositoryStream;
import hex.ql.ast.InvalidAstException;
import hex.repo.test.Person;
import hex.repo.test.PersonRepository;

import java.util.List;
import java.util.stream.Collectors;

import static hex.ql.QueryLanguage.*;

/**
 * Created by jason on 14-10-25.
 */
public class HexRepoMain {
    public static void main(String[] args) throws InvalidAstException {
        String sql, expected;
        Repository<Person> repo = new PersonRepository();
        sql = repo.stream().toSql();
        expected = "SELECT * FROM people";
        if(!expected.equals(sql)) System.exit(-1);

        RepositoryStream<Person> q = (RepositoryStream<Person>) repo.where(Person::getFirstName, is("Jason"));

        sql = q.toSql();
        expected = "SELECT * FROM people WHERE first_name = 'Jason'";
        if(!expected.equals(sql)) System.exit(1);

        sql = q.toPreparedSql();
        expected = "SELECT * FROM people WHERE first_name = ?";
        if(!expected.equals(sql)) System.exit(11);

        q = (RepositoryStream<Person>) q.where(Person::getLastName, is("Wall"));
        sql = q.toSql();
        expected = "SELECT * FROM people WHERE (first_name = 'Jason') AND (last_name = 'Wall')";
        if(!expected.equals(sql)) System.exit(2);

        sql = q.toPreparedSql();
        expected = "SELECT * FROM people WHERE (first_name = ?) AND (last_name = ?)";
        if(!expected.equals(sql)) System.exit(21);

        q = (RepositoryStream<Person>)repo.stream().filter(
                where(Person::getLastName, is("Wall"))
                        .and(where(Person::getFirstName, is("Jason")).or(where(Person::getFirstName, is("Natalie"))))
        );

        sql = q.toSql();
        expected = "SELECT * FROM people WHERE (last_name = 'Wall') AND ((first_name = 'Jason') OR (first_name = 'Natalie'))";
        if(!expected.equals(sql)) System.exit(3);

        List<Person> people = q.parallel().collect(Collectors.toList());
        if(people.size() != 2) System.exit(5);

        people.forEach((p) -> System.out.printf("%d:%s %s", p.getId(), p.getFirstName(), p.getLastName()).println());

        q = (RepositoryStream<Person>) q.where(Person::getFirstName, is("Bryce"));

        if(q.collect(Collectors.toList()).size() > 0) System.exit(6);

        q.filter((p) -> p.getFirstName().equals("Wayne")).forEach((p) -> System.exit(7));

        Person j = repo.find(1).get();
        if(!j.getFirstName().equals("Jason")) System.exit(8);
    }
}
