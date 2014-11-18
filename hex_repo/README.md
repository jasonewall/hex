Hex Repo
======================

ORM using the API declared in [hex/hexql](http://github.com/thejayvm/hex/hexql)

Use the `QueryLanguage` defined in JILL.

    import static hex.ql.QueryLangauge.*

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

### No byte code manipulation of "business classes"

It's a pretty common practice for a lot of JVM based ORMs out there. I'd like to see something that doesn't do it. At 
first I tried to do zero byte manipulation, but that seemed excessively idealistic. I'm breaking down and allowing the creating
of subclasses off of model objects.

### All queries map to POJOs

All mapping metadata should be able to be declared outside of the "Model" classes.

    NOTE: As of now I still haven't figured out how I'm going to declare custom mapping of fields to columns.


### All queries should implement the `Stream` interfaces from Java 8

A new directive I'm enforcing in JILL. It's going to require a lot of re-working what is currently here. I'm thinking
something like: `Queryable` -> `stream()` or `query()` that returns an object that implements the `Stream` interface
as well as an additional `Query` interface.

e.g. Calling `Stream#filter` on a Repository Query will translate itself to an addition the where clause.

### What to do?

That being said, here's the normal contributing ramble:

1. Fork it (`http://github.com/thejayvm/hex/fork`)
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create new Pull Request
6. If fixing reported issues please use `hub pull-request` to make a PR with the same ID as the issue. (https://github.com/github/hub)
