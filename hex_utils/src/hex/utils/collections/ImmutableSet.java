package hex.utils.collections;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 * Created by jason on 14-12-13.
 */
public class ImmutableSet<T> extends AbstractSet<T> {
    private final Set<T> entries;

    public ImmutableSet(Set<T> entries) {
        Objects.requireNonNull(entries);
        this.entries = entries;
    }

    /**
     * Returns an iterator over the elements contained in this collection.
     *
     * @return an iterator over the elements contained in this collection
     */
    @Override
    public Iterator<T> iterator() {
        Iterator<T> it = entries.iterator();
        if(!it.hasNext()) return it; // no use creating ANOTHER empty instance

        return new Iterator<T>() {
            /**
             * Returns {@code true} if the iteration has more elements.
             * (In other words, returns {@code true} if {@link #next} would
             * return an element rather than throwing an exception.)
             *
             * @return {@code true} if the iteration has more elements
             */
            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            /**
             * Returns the next element in the iteration.
             *
             * @return the next element in the iteration
             * @throws java.util.NoSuchElementException if the iteration has no more elements
             */
            @Override
            public T next() {
                return it.next();
            }
        };
    }

    @Override
    public int size() {
        return entries.size();
    }
}
