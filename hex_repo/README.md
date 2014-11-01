Hex Repo
======================

ORM using the API declared in [thejayvm/jill](http://github.com/thejayvm/jill)

Use the `QueryLanguage` defined in JILL.

    import static ca.thejayvm.jill.QueryLangauge.*

Instantiate your repo instance

    Repository<Person> people = new PersonRepository();

Start querying!

```java
from(people).where(field(Person::getLastName, is("Newton")).forEach((p) -> {
    System.out.println(p.getFirstName() + " " + p.getLastName());
});
```