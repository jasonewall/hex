package hex.repo.test;

import hex.repo.Repository;
import hex.repo.streams.RepositoryStream;

import java.util.List;
import java.util.stream.Collectors;

import static hex.ql.QueryLanguage.*;

/**
 * Created by jason on 14-10-25.
 */
public class HexRepoMain {
    public static void main(String[] args) {
        Repository<Person> repo = new PersonRepository();

        RepositoryStream<Person> q;

        q = (RepositoryStream<Person>)repo.stream().filter(
                where(Person::getLastName, is("Newton"))
                        .and(where(Person::getFirstName, is("Isaac")).or(where(Person::getFirstName, is("Wayne"))))
        );

        List<Person> people = q.parallel().collect(Collectors.toList());
        if(people.size() != 2) System.exit(5);

        people.forEach((p) -> System.out.printf("%d:%s %s", p.getId(), p.getFirstName(), p.getLastName()).println());

        q = (RepositoryStream<Person>) q.where(Person::getFirstName, is("Fig"));

        if(q.collect(Collectors.toList()).size() > 0) System.exit(6);

        q.filter((p) -> p.getFirstName().equals("Wayne")).forEach((p) -> System.exit(7));

        Person j = repo.find(7).get();
        if(!j.getFirstName().equals("Fig")) System.exit(8);
    }
}
