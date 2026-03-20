package model.state;

import model.statement.Statement;

import java.util.List;

public interface ExecutionStack<T> {
    void push(T statement);
    T pop();
    boolean isEmpty();
    List<T> getStack();
}
