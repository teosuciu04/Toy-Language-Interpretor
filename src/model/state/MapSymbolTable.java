package model.state;

import model.type.Type;
import model.value.Value;

import java.util.HashMap;
import java.util.Map;

public class MapSymbolTable<K, V extends Value> implements SymbolTable<K, V> {
    private final Map<K, V> map = new HashMap<>();

    @Override
    public boolean isDefined(K variableName) {
        return map.containsKey(variableName);
    }

    @Override
    public void put(K variableName, V value) {
        map.put(variableName, value);
    }

    @Override
    public V getValue(K variableName) {
        return map.get(variableName);
    }

    @Override
    public void update(K variableName, V value) {
        map.put(variableName, value);
    }

    @Override
    public Type getType(K variableName) {
        return map.get(variableName).getType();
    }

    @Override
    public Map<K, V> getSymbolTable() {
        return map;
    }

    @Override
    public String toString() {
        return map.toString();
    }

    @Override
    public SymbolTable<K, V> deepCopy() {
        MapSymbolTable<K, V> copy = new MapSymbolTable<>();
        copy.getSymbolTable().putAll(this.map);
        return copy;
    }

}
