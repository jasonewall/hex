package hex.repo.test;

import hex.ql.Query;
import hex.ql.ast.predicates.OrPredicate;
import hex.repo.RepositoryBase;
import hex.repo.metadata.Metadata;

import java.util.function.Predicate;
import java.util.stream.Stream;

import static hex.ql.QueryLanguage.is;
import static hex.ql.QueryLanguage.where;

/**
 * Created by jason on 14-10-26.
 */
public class PersonRepository extends RepositoryBase<Person> {
    private static Metadata<Person> metadata = Metadata.fromClass(Person.class);

    public PersonRepository() {
        super(metadata);
    }

    @Override
    public String getTableName() {
        return "people";
    }

    public Query<Person> familyMembers(String lastName, String... firstNames) {
        return stream().filter(
                where(Person::getLastName, is(lastName)).and(
                        Stream.of(firstNames).<Predicate<Person>>map(s -> where(Person::getFirstName, is(s)))
                        .reduce(OrPredicate::new).get()
                )
        );
    }
}
