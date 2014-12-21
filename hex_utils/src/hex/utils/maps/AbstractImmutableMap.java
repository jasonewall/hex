package hex.utils.maps;

import hex.utils.collections.ImmutableSet;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by jason on 14-12-13.
 */
public abstract class AbstractImmutableMap<K,V> extends AbstractMap<K,V> {
    private ImmutableSet<Entry<K,V>> entries;

    /**
     * @throws UnsupportedOperationException Map is immutable.
     */
    @Override
    public V put(K key, V value) {
        throw new UnsupportedOperationException("Map is immutable.");
    }

    /**
     * @throws UnsupportedOperationException If the specified map is nonempty.
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        if(m.isEmpty()) return;
        throw new UnsupportedOperationException("Map is immutable.");
    }

    /**
     * If this map does not contain this key this is a no-op invocation. Otherwise
     * throws an {@code UnsupportedOperationException}.
     * @throws UnsupportedOperationException If the map has this key.
     */
    @Override
    public V remove(Object key) {
        if(containsKey(key)) {
            throw new UnsupportedOperationException("Map is immutable.");
        }
        return null;
    }

    /**
     * Build the {@code entrySet} that will be converted into an immutable implementation of the {@link Set}
     * interface.
     *
     * This pattern is meant to be a convenience for creating implementations of an immutable {@link Map}
     * implementation.
     * @return A {@link Set} of entries
     */
    protected abstract Set<Entry<K,V>> buildEntries();

    @Override
    public Set<Entry<K, V>> entrySet() {
        if(this.entries == null) {
            Set<Entry<K, V>> entries = buildEntries();
            this.entries = new ImmutableSet<>(entries.stream().map(e -> {
                if(e instanceof SimpleImmutableEntry) return e;
                return new SimpleImmutableEntry<>(e);
            }).collect(HashSet::new, Collection::add, Collection::addAll));
        }
        return entries;
    }
}
