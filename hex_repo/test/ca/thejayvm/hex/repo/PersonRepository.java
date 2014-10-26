package ca.thejayvm.hex.repo;

/**
 * Created by jason on 14-10-26.
 */
public class PersonRepository extends RepositoryBase<Person> {
    public PersonRepository() {
        keyFields.put("first_name", "first_name");
        keyFields.put("last_name", "last_name");
        keyFields.put(1, "id");

        keyRecord = new Person();
        keyRecord.setFirstName("first_name");
        keyRecord.setLastName("last_name");
        keyRecord.setId(1);
    }
}
