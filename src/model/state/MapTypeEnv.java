package model.state;

import model.type.Type;

import java.util.HashMap;
import java.util.Map;

public class MapTypeEnv<K, V> implements TypeEnv<K, V> {

    private final Map<K, V> map = new HashMap<>();

    @Override
    public void put(K key, V value) {
        map.put(key, value);
    }

    @Override
    public V get(K key) {
        return map.get(key);
    }

    @Override
    public boolean isDefined(K key) {
        return map.containsKey(key);
    }

    @Override
    public TypeEnv<K, V> clone() {
        MapTypeEnv<K, V> newEnv = new MapTypeEnv<>();
        newEnv.map.putAll(this.map);
        return newEnv;
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
