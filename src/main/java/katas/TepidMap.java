package katas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.WeakHashMap;

public class TepidMap<K, V> implements Map<K, V> {
    private Map<K, V> strong = new HashMap<>();
    private Map<K, V> weak = new WeakHashMap<>();

    public TepidMap() {
    }

    public TepidMap(final Map<K, V> seed) {
        putAll(seed);
    }

    @Override
    public int size() {
        return strong.size() + weak.size();
    }

    @Override
    public boolean isEmpty() {
        return strong.isEmpty() && weak.isEmpty();
    }

    @Override
    public boolean containsKey(final Object key) {
        return strong.containsKey(key) || weak.containsKey(key);
    }

    @Override
    public boolean containsValue(final Object value) {
        return strong.containsValue(value) || weak.containsValue(value);
    }

    @Override
    public V get(final Object key) {
        return Optional.ofNullable(strong.get(key)).orElse(weak.get(key));
    }

    @Override
    public V put(final K key, final V value) {
        return put(key, value, false);
    }

    public V put(final K key, final V value, final boolean isStrong) {
        if (isStrong) {
            return put(key, value, strong, weak);
        } else {
            return put(key, value, weak, strong);
        }
    }

    private V put(final K key, final V value, Map<K, V> primary, Map<K, V> deprecated) {
        return Optional.ofNullable(primary.put(key, value)).orElse(deprecated.remove(key));
    }

    @Override
    public V remove(final Object key) {
        return Optional.ofNullable(strong.remove(key)).orElse(weak.remove(key));
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> m) {
        if (m instanceof TepidMap) {
            putAll(cast(m).strong, true);
            putAll(cast(m).weak, false);
        } else {
            putAll(m, false);
        }
    }

    @SuppressWarnings("unchecked")
    private TepidMap<? extends K, ? extends V> cast(final Map<? extends K, ? extends V> m) {
        return (TepidMap<? extends K, ? extends V>) m;
    }

    public void putAll(final Map<? extends K, ? extends V> m, final boolean isStrong) {
        for (Map.Entry<? extends K, ? extends V> e : m.entrySet())
            put(e.getKey(), e.getValue(), isStrong);
    }

    @Override
    public void clear() {
        strong.clear();
        weak.clear();
    }

    @Override
    public Set<K> keySet() {
        final Set<K> keys = new HashSet<>(strong.keySet());
        keys.addAll(weak.keySet());
        return Collections.unmodifiableSet(keys);
    }

    @Override
    public Collection<V> values() {
        final List<V> values = new ArrayList<>(strong.values());
        values.addAll(weak.values());
        return Collections.unmodifiableCollection(values);
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        final Set<Entry<K, V>> entries = new HashSet<>(strong.entrySet());
        entries.addAll(weak.entrySet());
        return Collections.unmodifiableSet(entries);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Map)) return false;
        return entrySet().equals(((Map<?, ?>) o).entrySet());
    }

    @Override
    public int hashCode() {
        return entrySet().hashCode();
    }

    @Override
    public String toString() {
        return "TepidMap{" +
            "strong=" + strong +
            ", weak=" + weak +
            '}';
    }
}
