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
        System.out.println(sql);
    }
}
