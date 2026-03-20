package model.state;

import java.util.*;

public class StackExecutionStack<T> implements ExecutionStack<T>{

    private final Deque<T> stack = new ArrayDeque<>();


    @Override
    public void push(T statement) {
        stack.addFirst(statement);
    }

    @Override
    public T pop() {
        return stack.removeFirst();
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public List<T> getStack() {
        return new ArrayList<>(stack);
    }

    @Override
    public String toString() {
        return stack.toString();
    }
}
