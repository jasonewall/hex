package ca.thejayvm.ca.hex.repo.examples;

import ca.thejayvm.hex.repo.Person;
import ca.thejayvm.hex.repo.PersonRepository;
import ca.thejayvm.hex.repo.Repository;

import static ca.thejayvm.jill.QueryLanguage.*;

/**
 * Created by jason on 14-11-01.
 */
public class HexRepoExamples {
    public static void main(String[] args) {
        Repository<Person> people = new PersonRepository();

//        from(people).where(field(Person::getLastName, is("Newton")));
    }
}
