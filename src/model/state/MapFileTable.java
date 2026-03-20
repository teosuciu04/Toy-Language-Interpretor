package model.state;

import model.value.StringValue;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

public class MapFileTable implements FileTable {
    private final Map<StringValue, BufferedReader> table;

    public MapFileTable() {
        this.table = new HashMap<>();
    }

    @Override
    public void add(StringValue filename, BufferedReader reader) {
        table.put(filename, reader);
    }

    @Override
    public BufferedReader lookup(StringValue filename) {
        return table.get(filename);
    }

    @Override
    public boolean isDefined(StringValue filename) {
        return table.containsKey(filename);
    }

    @Override
    public void remove(StringValue filename) {
        table.remove(filename);
    }

    @Override
    public Map<StringValue, BufferedReader> getContent() {
        return table;
    }

    @Override
    public String toString() {
        return table.toString();
    }
}
