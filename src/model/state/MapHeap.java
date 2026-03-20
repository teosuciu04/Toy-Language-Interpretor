package model.state;

import model.value.Value;

import java.util.HashMap;
import java.util.Map;

public class MapHeap implements Heap{

    private Map<Integer, Value> heap = new HashMap<Integer, Value>();
    private int nextFree = 1;

    @Override
    public int allocate(Value value) {
        int address = nextFree;
        heap.put(address, value);
        nextFree++;
        return address;
    }

    @Override
    public boolean isDefined(int address) {
        return heap.containsKey(address);
    }

    @Override
    public Map<Integer, Value> getContent() {
        return heap;
    }

    @Override
    public Value read(int address) {
        return heap.get(address);
    }

    @Override
    public void write(int address, Value value) {
        heap.put(address, value);
    }

    @Override
    public void setContent(Map<Integer, Value> newContent) {
        heap = newContent;
    }

    @Override
    public String toString() {
        return heap.toString();
    }
}
