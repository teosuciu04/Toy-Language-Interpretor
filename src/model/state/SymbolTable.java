package model.state;

import model.type.Type;

import java.util.Map;

public interface SymbolTable<K, V> {
    boolean isDefined(K variableName);
    void put(K variableName, V value);
    V getValue(K variableName);
    void update(K variableName, V value);
    Type getType(K variableName);
    Map<K, V> getSymbolTable();

    SymbolTable<K, V> deepCopy();

}
