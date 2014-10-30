package ca.thejayvm.hex.repo;

/**
 * Created by jason on 14-10-26.
 */
public class PersonRepository extends RepositoryBase<Person> {
    public PersonRepository() {
        Person keyRecord = new Person();
        keyRecord.setFirstName("first_name");
        keyRecord.setLastName("last_name");
        keyRecord.setId(1);

        metadata = new Metadata<>(keyRecord);
        metadata.registerField("first_name", "first_name");
        metadata.registerField("last_name", "last_name");
        metadata.registerField(1, "id");

        metadata.registerSetter("first_name", "setFirstName", String.class);
        metadata.registerSetter("last_name", "setLastName", String.class);
        metadata.registerSetter("id", "setId", int.class);
    }

    @Override
    protected String getTableName() {
        return "people";
    }
}
