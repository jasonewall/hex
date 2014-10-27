package ca.thejayvm.hex.repo;

import ca.thejayvm.jill.Query;
import ca.thejayvm.jill.ast.InvalidAstException;

import static ca.thejayvm.jill.QueryLanguage.*;

/**
 * Created by jason on 14-10-25.
 */
public class HexRepoMain {
    public static void main(String[] args) throws InvalidAstException {
        PersonRepository repo = new PersonRepository();
        Query<Person> q = repo.where(field(Person::getFirstName, is("Jason")));

        String sql = repo.toSql(q);
        String expected = "SELECT * FROM people WHERE first_name = 'Jason'";
        if(!expected.equals(sql)) System.exit(1);

        q.where(field(Person::getLastName, is("Wall")));
        sql = repo.toSql(q);
        expected = "SELECT * FROM people WHERE (first_name = 'Jason') AND (last_name = 'Wall')";
        if(!expected.equals(sql)) System.exit(2);

        q = repo.where(
                field(Person::getLastName, is("Wall"))
                    .and(field(Person::getFirstName, is("Jason")).or(field(Person::getFirstName, is("Natalie"))))
        );

        sql = repo.toSql(q);
        expected = "SELECT * FROM people WHERE (last_name = 'Wall') AND ((first_name = 'Jason') OR (first_name = 'Natalie'))";
        if(!expected.equals(sql)) System.exit(3);
    }
}
