package hex.repo.streams;

import hex.repo.AbstractRepository;
import hex.repo.QueryResult;
import hex.ql.queries.AbstractQuery;
import hex.ql.Query;
import hex.ql.ast.*;
import hex.ql.ast.predicates.NullPredicate;
import hex.ql.queries.StreamQuery;
import hex.repo.RepositoryException;
import hex.repo.metadata.DataMappingException;
import hex.repo.sql.SqlQuery;

import java.sql.SQLException;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * Created by jason on 14-11-16.
 */
public class RepositoryStream<T> extends AbstractQuery<T> implements Stream<T> {
    private AbstractRepository<T> repository;

    private boolean distinct = false;

    private long limit = -1;

    private long offset = -1;

    private boolean parallel = false;

    private Consumer<T> peeker = (t) -> {};

    private Predicate<T> predicate = new NullPredicate<>();

    private List<RepositoryStream> joins = new ArrayList<>();

    private Node[] where;

    private Node[] orderBy;

    private RepositoryStream<T> duplicate() {
        RepositoryStream<T> dup = new RepositoryStream<>(repository);
        dup.distinct = distinct;
        dup.limit = limit;
        dup.offset = offset;
        dup.parallel = parallel;
        dup.peeker = peeker;
        dup.predicate = predicate;
        dup.joins = joins;
        dup.where = where;
        dup.orderBy = orderBy;
        return dup;
    }

    private Stream<T> terminateRepositoryStream() {
        Stream<T> stream = StreamSupport.stream(spliterator(), this.isParallel());
        //TODO: maybe we should pass in the close handlers too?
        stream = stream.peek(peeker);
        return stream;
    }

    public String toSql() {
        SqlQuery result = new SqlQuery(repository.get_metadata());
        result.from(new Node[]{new Variable(repository.getTableName())});
        result.where(where);
        try {
            return result.toSql();
        } catch(InvalidAstException e) {
            throw new RepositoryException(e);
        }
    }

    public RepositoryStream(AbstractRepository<T> repository) {
        this.repository = repository;
    }

    /**
     * Returns a stream resulting from applying the predicate in the form of a
     * where clause to this stream.
     * If the predicate cannot be converted to an SQL AST, this stream is converted
     * to a {@link java.util.List} and then passes the predicate to a stream of the resulting
     * @{@link java.util.List}. This is considered a database terminal operation.
     * <p>
     * <p>This is an <a href="package-summary.html#StreamOps">intermediate
     * operation</a>.
     *
     * @param predicate a <a href="package-summary.html#NonInterference">non-interfering</a>,
     *                  <a href="package-summary.html#Statelessness">stateless</a>
     *                  predicate to apply to each element to determine if it
     *                  should be included
     * @return the new stream
     */
    @Override
    public Query<T> filter(Predicate<? super T> predicate) {
        RepositoryStream<T> dup = duplicate();
        dup.predicate = this.predicate.and(predicate);
        if(predicate instanceof Node) {
            try {
                dup.where = ((Node)dup.predicate).toTree();
                return dup;
            } catch (InvalidAstException e) {
                // ignore and fallback to normal stream mode
            }
        }
        return new StreamQuery<>(terminateRepositoryStream().filter(predicate));
    }

    /**
     * Returns a stream consisting of the results of applying the given
     * function to the elements of this stream.
     *
     * In the database sense, should be used to adjust your result into:
     * <ol>
     *     <li>A single column not represented by the native map methods</li>
     *     <li>Mapping additional columns to a new class type in more complicated queries</li>
     *     <li>Possibly mapping to a One-to-one or a Many-to-one relationship</li>
     * </ol>
     *
     * <p>This is an <a href="package-summary.html#StreamOps">intermediate
     * operation</a>.
     *
     * @param <R> The element type of the new stream
     * @param mapper a <a href="package-summary.html#NonInterference">non-interfering</a>,
     *               <a href="package-summary.html#Statelessness">stateless</a>
     *               function to apply to each element
     * @return the new stream
     */
    @Override
    public <R> Query<R> map(Function<? super T, ? extends R> mapper) {
        return super.map(mapper);
    }

    /**
     * Returns a {@code RepositoryIntStream} that is essentially a single column
     * query that has an {@code int} for a single column. Once terminated the results
     * are delegated to a traditional {@link java.util.stream.IntStream}. Destroys the
     * existing {@code SELECT} clause if there was one.
     * <p>
     * <p>This is an <a href="package-summary.html#StreamOps">
     * intermediate operation</a>.
     *
     * @param mapper a <a href="package-summary.html#NonInterference">non-interfering</a>,
     *               <a href="package-summary.html#Statelessness">stateless</a>
     *               function to apply to each element
     * @return the new stream
     */
//    @Override
//    public IntStream mapToInt(ToIntFunction<? super T> mapper) {
//        return null;
//    }

    /**
     * Returns a stream consisting of the results of replacing each element of
     * this stream with the contents of a mapped stream produced by applying
     * the provided mapping function to each element.
     *
     * This is effectively this is an inner join to a one-to-many relationship in a
     * relational database.
     * <p>
     * <p>This is an <a href="package-summary.html#StreamOps">intermediate
     * operation</a>.
     *
     * @param mapper a <a href="package-summary.html#NonInterference">non-interfering</a>,
     *               <a href="package-summary.html#Statelessness">stateless</a>
     *               function to apply to each element which produces a stream
     *               of new values
     * @return the new stream
     * The {@code flatMap()} operation has the effect of applying a one-to-many
     * transformation to the elements of the stream, and then flattening the
     * resulting elements into a new stream.
     * <p>
     * <p><b>Examples.</b>
     * <p>
     * <p>If {@code orders} is a stream of purchase orders, and each purchase
     * order contains a collection of line items, then the following produces a
     * stream containing all the line items in all the orders:
     * <pre>
     *     orders.flatMap(order -> order.getLineItems().stream())...
     * </pre>
     * <p>
     */
    @Override
    public <R> Query<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper) {
        RepositoryStream<R> newStream = repository.get_metadata().mapToRepositoryStream(mapper);
        newStream.joins.add(this);
        return newStream;
    }

    /**
     * Indicates that the query will call {@code DISTINCT} when it queries
     * the database.
     * <p>
     * <p>For ordered streams, the selection of distinct elements is stable
     * (for duplicated elements, the element appearing first in the encounter
     * order is preserved.)  For unordered streams, no stability guarantees
     * are made.
     * <p>
     * <p>This is a <a href="package-summary.html#StreamOps">stateful
     * intermediate operation</a>.
     *
     * @return the new stream
     */
    @Override
    public Query<T> distinct() {
        RepositoryStream<T> dup = duplicate();
        dup.distinct = true;
        return dup;
    }

    /**
     * Returns a stream consisting of the elements of this stream, sorted
     * according to natural order. As the natural order of database records
     * is always in affect, this does the same as {@link #unordered()}
     * <p>
     * <p>For ordered streams, the sort is stable.  For unordered streams, no
     * stability guarantees are made.
     * <p>
     * <p>This is a <a href="package-summary.html#StreamOps">stateful
     * intermediate operation</a>.
     *
     * @return the new stream
     */
    @Override
    public Query<T> sorted() {
        return unordered();
    }

    /**
     * Returns a stream consisting of the elements of this stream, additionally
     * performing the provided action on each element as elements are consumed
     * from the resulting query's {@link java.sql.ResultSet}.
     *
     * If subsequent actions cause this RepositoryStream to be terminated before
     * execution, the collection of peek actions are passed on to the resulting
     * {@link hex.ql.queries.StreamQuery} before returning the new stream.
     * <p>
     * <p>This is an <a href="package-summary.html#StreamOps">intermediate
     * operation</a>.
     * <p>
     * <p>For parallel stream pipelines, the action may be called at
     * whatever time and in whatever thread the element is made available by the
     * upstream operation.  If the action modifies shared state,
     * it is responsible for providing the required synchronization.
     *
     * @param action a <a href="package-summary.html#NonInterference">
     *               non-interfering</a> action to perform on the elements as
     *               they are consumed from the stream
     * @return the new stream
     * <pre>
     *     Stream.of("one", "two", "three", "four")
     *         .filter(e -> e.length() > 3)
     *         .peek(e -> System.out.println("Filtered value: " + e))
     *         .map(String::toUpperCase)
     *         .peek(e -> System.out.println("Mapped value: " + e))
     *         .collect(Collectors.toList());
     * </pre>
     */
    @Override
    public Query<T> peek(Consumer<? super T> action) {
        RepositoryStream<T> dup = duplicate();
        dup.peeker = peeker.andThen(action);
        return dup;
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        try(QueryResult<T> results = iterator()) {
            results.forEachRemaining(peeker.andThen(action::accept));
        }
    }

    /**
     * Sets the {@code LIMIT} clause of the query being created by this stream
     * and returns the resulting stream.
     * <p>
     * <p>This is a <a href="package-summary.html#StreamOps">short-circuiting
     * stateful intermediate operation</a>.
     *
     * @param maxSize the number of elements the stream should be limited to
     * @return the new stream
     * @throws IllegalArgumentException if {@code maxSize} is negative
     */
    @Override
    public Query<T> limit(long maxSize) {
        RepositoryStream<T> dup = duplicate();
        dup.limit = maxSize;
        return dup;
    }

    @Override
    public Optional<T> findFirst() {
        return repository.executeQuery(toSql(), (rs) -> {
            if(!rs.next())
                return Optional.<T>empty();

            return Optional.of(repository.get_metadata().mapRecord(rs));
        });
    }

    /**
     * Sets the {@code OFFSET} clause of the query being created by this stream
     * and returns the resulting stream.
     *
     * If this stream contains fewer than {@code n} elements then an
     * empty stream will be returned.
     * <p>
     * <p>This is a <a href="package-summary.html#StreamOps">stateful
     * intermediate operation</a>.
     *
     * @param n the number of leading elements to skip
     * @return the new stream
     * @throws IllegalArgumentException if {@code n} is negative
     */
    @Override
    public Query<T> skip(long n) {
        RepositoryStream<T> dup = duplicate();
        dup.offset = n;
        return dup;
    }

    /**
     * Returns whether this stream, if a terminal operation were to be executed,
     * would execute in parallel.  Calling this method after invoking an
     * terminal stream operation method may yield unpredictable results.
     *
     * @return {@code true} if this stream would execute in parallel if executed
     */
    @Override
    public boolean isParallel() {
        return parallel;
    }

    /**
     * Returns an equivalent stream that is sequential.  May return
     * itself, either because the stream was already sequential, or because
     * the underlying stream state was modified to be sequential.
     * <p>
     * <p>This is an <a href="package-summary.html#StreamOps">intermediate
     * operation</a>.
     *
     * @return a sequential stream
     */
    @Override
    public Query<T> sequential() {
        if(!parallel) return this;
        RepositoryStream<T> dup = duplicate();
        dup.parallel = false;
        return dup;
    }

    /**
     * Returns a {@link hex.repo.streams.RepositoryStream} that when a database
     * terminal operation is executed, ensures the resulting {@link java.util.List#stream()}
     * is also parallel.
     * <p>
     * <p>This is an <a href="package-summary.html#StreamOps">intermediate
     * operation</a>.
     *
     * @return a parallel stream
     */
    @Override
    public Query<T> parallel() {
        RepositoryStream<T> dup = duplicate();
        dup.parallel = true;
        return dup;
    }

    @Override
    public QueryResult<T> iterator() {
        return new QueryResult<>(repository, toSql()).peek(peeker);
    }

    @Override
    public Spliterator<T> spliterator() {
        Iterator<T> it = iterator();
        return new Spliterator<T>() {
            @Override
            public boolean tryAdvance(Consumer<? super T> action) {
                if(it.hasNext()) {
                    action.accept(it.next());
                    return true;
                }
                return false;
            }

            @Override
            public Spliterator<T> trySplit() {
                return Spliterators.emptySpliterator();
            }

            @Override
            public long estimateSize() {
                return 0;
            }

            @Override
            public int characteristics() {
                return 0;
            }
        };
    }

    @Override
    public <R, A> R collect(Collector<? super T, A, R> collector) {
        A a = collector.supplier().get();
        try(QueryResult<T> results = iterator()) {
            results.forEachRemaining(peeker.andThen(t -> collector.accumulator().accept(a, t)));
        }
        return collector.finisher().apply(a);
    }

    /**
     * Nukes the order by clause of this stream if there is one and returns
     * the resulting stream. If there is no order by it returns itself.
     *
     * <p>This is an <a href="package-summary.html#StreamOps">intermediate
     * operation</a>.
     *
     * @return an unordered stream
     */
    @Override
    public Query<T> unordered() {
        if(this.orderBy == null) return this;
        RepositoryStream<T> dup = duplicate();
        dup.orderBy = null;
        return dup;
    }
}
