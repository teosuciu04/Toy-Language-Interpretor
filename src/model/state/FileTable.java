package model.state;

import model.value.StringValue;

import java.io.BufferedReader;
import java.util.Map;

public interface FileTable {
    void add(StringValue filename, BufferedReader reader);
    BufferedReader lookup(StringValue filename);
    boolean isDefined(StringValue filename);
    void remove(StringValue filename);
    Map<StringValue, BufferedReader> getContent();
}
