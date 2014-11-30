package hex.repo.streams;

import hex.repo.AbstractRepository;

import java.util.Iterator;
import java.util.function.Consumer;

/**
 * Created by jason on 14-11-29.
 */
public class RepositoryIterator<T> implements Iterator<T> {
    private AbstractRepository<T> repository;

    private String query;

    private Consumer<T> peekers;

    private Iterator<T> iterator;

    public RepositoryIterator(AbstractRepository<T> repository, String query, Consumer<T> peekers) {
        this.repository = repository;
        this.query = query;
        this.peekers = peekers;
    }

    /**
     * Returns {@code true} if the iteration has more elements.
     * (In other words, returns {@code true} if {@link #next} would
     * return an element rather than throwing an exception.)
     *
     * @return {@code true} if the iteration has more elements
     */
    @Override
    public boolean hasNext() {
        return internalIterator().hasNext();
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws java.util.NoSuchElementException if the iteration has no more elements
     */
    @Override
    public T next() {
        T t = internalIterator().next();
        peekers.accept(t);
        return t;
    }

    private Iterator<T> internalIterator() {
        if(iterator == null) {
            iterator = repository.executeQuery(query).iterator();
        }

        return iterator;
    }
}
