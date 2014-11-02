package ca.thejayvm.hex.repo;

/**
 * Created by jason on 14-10-26.
 */
public class PersonRepository extends RepositoryBase<Person> {
    private static Metadata<Person> metadata = Metadata.fromClass(Person.class);
    protected Metadata<Person> get_metadata() { return metadata; }

    @Override
    protected String getTableName() {
        return "people";
    }
}
