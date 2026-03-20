package model.state;

import java.util.ArrayList;
import java.util.List;

public class ListOut<T> implements Out<T>{
    private final List<T> outputList;

    public ListOut() {
        outputList = new ArrayList<>();
    }

    @Override
    public void add(T value) {
        outputList.add(value);
    }

    @Override
    public List<T> getOut() {
        return  outputList;
    }

    @Override
    public String toString() {
        return outputList.toString();
    }
}
