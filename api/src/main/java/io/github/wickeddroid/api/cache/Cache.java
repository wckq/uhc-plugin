package io.github.wickeddroid.api.cache;

import java.util.Collection;

/**
 * Simple Cache Map
 * @param <K> Key
 * @param <V> Value
 */
public interface Cache<K, V> {
  void save(K key, V value);

  void invalidate(K key);

  V get(K key);

  Collection<V> values();
}
