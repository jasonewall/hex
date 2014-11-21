package hex.ql.queries;

import hex.ql.Query;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.*;
import java.util.stream.*;

/**
 * Only reason to extend from this is to lazily have all the methods implemented by
 * throwing an {@code UnsupportedOperationException}
 */
public abstract class AbstractQuery<T> implements Query<T> {
    @Override
    public <R> Query<R> map(Function<? super T, ? extends R> mapper) {
        throw new UnsupportedOperationException();
    }

    @Override
    public IntStream mapToInt(ToIntFunction<? super T> mapper) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LongStream mapToLong(ToLongFunction<? super T> mapper) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DoubleStream mapToDouble(ToDoubleFunction<? super T> mapper) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <R> Query<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper) {
        throw new UnsupportedOperationException();
    }

    @Override
    public IntStream flatMapToInt(Function<? super T, ? extends IntStream> mapper) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LongStream flatMapToLong(Function<? super T, ? extends LongStream> mapper) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DoubleStream flatMapToDouble(Function<? super T, ? extends DoubleStream> mapper) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Query<T> filter(Predicate<? super T> predicate) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Query<T> distinct() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Query<T> sorted() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Query<T> sorted(Comparator<? super T> comparator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Query<T> peek(Consumer<? super T> action) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Query<T> limit(long maxSize) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Query<T> skip(long n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void forEachOrdered(Consumer<? super T> action) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <A> A[] toArray(IntFunction<A[]> generator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T reduce(T identity, BinaryOperator<T> accumulator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<T> reduce(BinaryOperator<T> accumulator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <R, A> R collect(Collector<? super T, A, R> collector) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<T> min(Comparator<? super T> comparator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<T> max(Comparator<? super T> comparator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long count() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean anyMatch(Predicate<? super T> predicate) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean allMatch(Predicate<? super T> predicate) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean noneMatch(Predicate<? super T> predicate) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<T> findFirst() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<T> findAny() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns an iterator for the elements of this stream.
     *
     * <p>This is a <a href="package-summary.html#StreamOps">terminal
     * operation</a>.
     *
     * @return the element iterator for this stream
     */
    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Spliterator<T> spliterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isParallel() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Query<T> sequential() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Query<T> parallel() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Query<T> unordered() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Query<T> onClose(Runnable closeHandler) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException();
    }
}
