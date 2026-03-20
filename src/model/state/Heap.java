package model.state;

import model.value.Value;

import java.util.Map;

public interface Heap {
    int allocate(Value value);
    boolean isDefined(int address);
    Map<Integer,Value> getContent();
    Value read(int address);
    void write(int address, Value value);

    void setContent(Map<Integer, Value> newContent);
}
