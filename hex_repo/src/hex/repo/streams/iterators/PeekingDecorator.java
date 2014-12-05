package hex.repo.streams.iterators;

import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Created by jason on 14-11-30.
 */
public class PeekingDecorator<T> extends AbstractPeekingSpliterator<T> {
    private Spliterator<T> spliterator;

    public PeekingDecorator(Spliterator<T> spliterator, Consumer<T> peekers) {
        super(peekers);
        this.spliterator = spliterator;
    }

    Spliterator<T> internalSpliterator() {
        return spliterator;
    }
}
