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
   ```java
   from(people).where(field(Person::getLastName, is("Newton"))
   ```
   ... will not execute the query on the database. It is only when you call a resolution expression that the database will
   be queried. For example, it was not until  we called `forEach()` in the above sample (a method on the `java.util.Iterable`    interface) that the database was queried and we started retrieving results.

2. Unless you manually call `toList()` on a repo-based `Queryable`, records are loaded as needed.

## Configuring your database access

The Repo `ConnectionManager` looks for the `hex_repo.properties` file in the root of your classpath. For now it's got a
pretty basic config for connecting to a database.

```properties
driver: org.postgresql.Driver
url: jdbc:postgresql:hex_repo_test
username: pg
password:
```

## Contributing

I'll welcome contributors but keep in mind the following:

### No byte code manipulation

It's a pretty common practice for a lot of JVM based ORMs out there. I'd like to see something that doesn't do it.

### All queries map to POJOs

All mapping metadata should be declared outside of the "Model" classes.

    NOTE: As of now I still haven't figured out how I'm going to declare custom mapping of fields to columns.

### All queries should be able to be queried by JILL

This is a sister project of [thejayvm/jill](https://github.com/thejayvm/jill). The idea behind that project is a Java written
query language for querying [java.util.Collection](http://docs.oracle.com/javase/7/docs/api/java/util/Collection.html)
instances. All of the results of querying in HexRepo implement a construct from JILL in some form or fashion.

### What to do?

That being said, here's the normal contributing ramble:

1. Fork it (`http://github.com/thejayvm/hex/fork`)
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create new Pull Request
6. If fixing reported issues please use `hub pull-request` to make a PR with the same ID as the issue. (https://github.com/github/hub)
