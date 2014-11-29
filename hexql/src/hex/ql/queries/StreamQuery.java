package hex.ql.queries;

import hex.ql.Query;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.*;

/**
 * Created by jason on 14-11-17.
 */
public class StreamQuery<T> extends AbstractQuery<T> {
    private Stream<T> stream;

    public StreamQuery(Stream<T> stream) {
        this.stream = stream;
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        stream.forEach(action);
    }

    @Override
    public Query<T> filter(Predicate<? super T> predicate) {
        StreamQuery<T> dup = duplicate();
        dup.stream = stream.filter(predicate);
        return dup;
    }

    @Override
    public <R> Query<R> map(Function<? super T, ? extends R> mapper) {
        return new StreamQuery<R>(stream.map(mapper));
    }

    @Override
    public <R, A> R collect(Collector<? super T, A, R> collector) {
        return stream.collect(collector);
    }

    private StreamQuery<T> duplicate() {
        StreamQuery<T> dup = new StreamQuery<>(stream);
        dup.stream = stream;
        return dup;
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
        return stream.iterator();
    }
}
