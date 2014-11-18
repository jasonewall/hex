package hex.ql;

import hex.ql.test.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static hex.ql.QueryLanguage.*;

/**
 * Created by jason on 14-10-25.
 */
public class Main {
    public static void main(String[] args) {
        Person p1 = new Person();
        p1.setFirstName("Jason");
        p1.setLastName("Wall");

        Person p2 = new Person();
        p2.setFirstName("Natalie");
        p2.setLastName("Wall");

        List<Person> people = new ArrayList<>();
        people.add(p1);
        people.add(p2);

        Supplier<Stream<Person>> s = () -> from(people).where(Person::getLastName, is("Wall"));

        if(s.get().collect(Collectors.toList()).size() != 2)
            System.exit(2);

        p2.setLastName("Paradis");

        if(s.get().collect(Collectors.toList()).size() != 1)
            System.exit(1);

        select(people, (p) -> "Bryce".equals(p.getFirstName())).forEach((p) -> System.exit(-1));

        from(people).where(Person::getLastName, is("Newton")).forEach((p) -> System.exit(-2));

        from(people).where(Person::getLastName, is("Paradis")).forEach((p) -> {
            if (!p.getFirstName().equals("Natalie")) System.exit(-3);
        });
    }
}

