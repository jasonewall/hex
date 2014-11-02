package ca.thejayvm.hex.repo;

import ca.thejayvm.jill.Query;
import ca.thejayvm.jill.Queryable;
import ca.thejayvm.jill.ast.InvalidAstException;

import java.util.List;

import static ca.thejayvm.jill.QueryLanguage.*;

/**
 * Created by jason on 14-10-25.
 */
public class HexRepoMain {
    public static void main(String[] args) throws InvalidAstException {
        RepositoryBase<Person> repo = new PersonRepository();
        repo = repo.where(field(Person::getFirstName, is("Jason")));

        String sql = repo.toSql();
        String expected = "SELECT * FROM people WHERE first_name = 'Jason'";
        if(!expected.equals(sql)) System.exit(1);

        repo = repo.where(field(Person::getLastName, is("Wall")));
        sql = repo.toSql();
        expected = "SELECT * FROM people WHERE (first_name = 'Jason') AND (last_name = 'Wall')";
        if(!expected.equals(sql)) System.exit(2);

        repo = new PersonRepository().where(
                field(Person::getLastName, is("Wall"))
                    .and(field(Person::getFirstName, is("Jason")).or(field(Person::getFirstName, is("Natalie"))))
        );

        sql = repo.toSql();
        expected = "SELECT * FROM people WHERE (last_name = 'Wall') AND ((first_name = 'Jason') OR (first_name = 'Natalie'))";
        if(!expected.equals(sql)) System.exit(3);

        List<Person> people = repo.toList();
        if(people == PersonRepository.LIST_ERROR) {
            repo.getExceptions().forEach(Exception::printStackTrace);
            System.exit(4);
        }

        if(people.size() != 2) System.exit(5);

        people.forEach((p) -> {
            System.out.println(String.format("%d:%s %s", p.getId(), p.getFirstName(), p.getLastName()));
        });
    }
}
