hex_repo
======================

ORM using the API declared in [hex/hexql](https://github.com/thejayvm/hex/tree/master/hexql)

Use the `QueryLanguage` helpers by creating a static import for them.

    import static hex.ql.QueryLangauge.*

Instantiate your repo instance

    Repository<Person> repo = new PersonRepository();

Start querying!

```java
repo.where(Person::getLastName, is("Newton"))
        .forEach(p -> System.out.println(p.getFirstName() + " " + p.getLastName());
```

Some things to keep in mind:

1. Queries are lazy. That means that just executing:
   ```java
   repo.where(Person::getLastName, is("Newton"))
   ```
   ... will not execute the query on the database. It is only when you call a resolution expression that the database will
   be queried. For example, it was not until  we called `forEach()` in the above sample that the database was queried and we started retrieving results.

2. The `java.util.stream.Collectors` work well on `Query` instances too, since `Query` implements `java.util.Stream`. So to get a list of `Query` results.
   ```java
   List<Person> newtons = from(people).where(Person::getLatsName, is("Newton")).collect(Collectors.toList());
   ```

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

I would love some contributers but keep in mind the following:

### No byte code manipulation of "business classes"

It's a pretty common practice for a lot of JVM based ORMs out there. I'd like to see something that doesn't do it. At
first I tried to do zero byte manipulation, but that seemed excessively idealistic. I'm breaking down and allowing the creating
of subclasses off of model objects.

### All queries map to POJOs

All mapping metadata should be able to be declared outside of the "Model" classes.

    NOTE: As of now I still haven't figured out how I'm going to declare custom mapping of fields to columns.


### All queries should implement the `Stream` interfaces from Java 8

This is pretty much already done now. Checkout `hex.repo.streams.RepositoryStream`. As of this writing there ar still a number of methos that need implementing, but the groundwork is all there.

e.g. Calling `Stream#filter` on a Repository Query will translate itself to an addition to the where clause.

### What to do?

That being said, here's the normal contributing ramble:

1. Fork it (`http://github.com/thejayvm/hex/fork`)
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create new Pull Request
6. If fixing reported issues please use `hub pull-request` to make a PR with the same ID as the issue. (https://github.com/github/hub)
