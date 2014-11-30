package hex.repo.test;

/**
 * Created by jason on 14-10-25.
 */
public class Person {
    private int id;

    private String lastName;

    private String firstName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }
}
