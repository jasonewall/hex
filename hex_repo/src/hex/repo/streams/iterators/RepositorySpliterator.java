package hex.repo.streams.iterators;

import hex.repo.AbstractRepository;
import hex.repo.sql.PreparedSqlQuery;

import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Created by jason on 14-11-29.
 */
public class RepositorySpliterator<T> extends AbstractPeekingSpliterator<T> {
    private final AbstractRepository<T> repository;

    private final PreparedSqlQuery query;

    private Spliterator<T> spliterator;

    public RepositorySpliterator(AbstractRepository<T> repository, PreparedSqlQuery query, Consumer<T> peekers) {
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
