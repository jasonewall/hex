package hex.repo.examples;

import hex.repo.Person;
import hex.repo.PersonRepository;
import hex.repo.Repository;

import static hex.ql.QueryLanguage.*;

public class HexRepoExamples {
    public static void main(String[] args) {
        Repository<Person> people = new PersonRepository();

        from(people).where(Person::getLastName, is("Newton"))
                .forEach((p) -> System.out.println(p.getFullName()));
    }
}
