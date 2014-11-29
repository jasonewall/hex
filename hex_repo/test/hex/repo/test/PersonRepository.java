package hex.repo.test;

import hex.repo.RepositoryBase;
import hex.repo.metadata.Metadata;

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
}