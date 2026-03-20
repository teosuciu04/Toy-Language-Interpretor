package model.state;

public interface TypeEnv<K,V> {
    void put(K key, V value);
    V get(K key);
    boolean isDefined(K key);
    TypeEnv<K, V> clone();
}
