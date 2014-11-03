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

Some things to keep in mind:

 1. Queries are lazy. That means that just executing:

        from(people).where(field(Person::getLastName, is("Newton"))

... will not execute the query on the database. It is only when you call a resolution expression that the database will
be queried. For example, it was not until  we called `forEach()` in the above sample (a method on the `java.util.Iterable` interface)
that the database was queried and we started retrieving results.

 1. Unless you manually call `toList()` on a repo base `Queryable`, records are loaded as needed.

## Configuring your database access

The Repo `ConnectionManager` looks for the `hex_repo.properties` file in the root of your classpath. For now it's got a
pretty basic config for connecting to a database.

```properties
driver: org.postgresql.Driver
url: jdbc:postgresql:hex_repo_test
username: pg
password:
```
