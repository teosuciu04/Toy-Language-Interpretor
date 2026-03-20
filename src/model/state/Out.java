package model.state;

import java.util.List;

public interface Out<T> {
    void add(T value);
    List<T> getOut();
}
