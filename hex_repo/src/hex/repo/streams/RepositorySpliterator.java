package hex.repo.streams;

import hex.repo.AbstractRepository;

import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Created by jason on 14-11-29.
 */
public class RepositorySpliterator<T> extends AbstractPeekingSpliterator<T> {
    private final AbstractRepository<T> repository;

    private final String query;

    private Spliterator<T> spliterator;

    public RepositorySpliterator(AbstractRepository<T> repository, String query, Consumer<T> peekers) {
        super(peekers);
        this.repository = repository;
        this.query = query;
    }

    synchronized Spliterator<T> internalSpliterator() {
        if(this.spliterator == null) {
            this.spliterator = repository.executeQuery(query).spliterator();
        }

        return spliterator;
    }
}
