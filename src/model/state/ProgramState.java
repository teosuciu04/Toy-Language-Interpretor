package model.state;

import model.statement.Statement;
import model.value.Value;
import exceptions.EmptyStackException;


import java.util.List;

public class ProgramState {
    private final ExecutionStack<Statement> executionStack;
    private final SymbolTable<String, Value> symbolTable;
    private final Out<Value> out;
    private final FileTable fileTable;
    private final Statement originalProgram;
    private final Heap heap;

    private final int id;
    private static int lastId = 0;

    private static synchronized int newId() {
        lastId++;
        return lastId;
    }

    public int getId() {
        return id;
    }


    public ProgramState(ExecutionStack<Statement> executionStack, SymbolTable<String,Value> symbolTable, Out<Value> out, FileTable fileTable, Statement originalProgram, Heap heap) {
        this.executionStack = executionStack;
        this.symbolTable = symbolTable;
        this.out = out;
        this.fileTable = fileTable;
        this.originalProgram = originalProgram;
        this.heap = heap;
        this.id = newId();
        //executionStack.push(originalProgram);
    }

    public SymbolTable<String,Value> getSymbolTable() {
        return symbolTable;
    }

    public ExecutionStack<Statement> getExecutionStack() {
        return executionStack;
    }

    public Out<Value> getOut() {return out;}

    public FileTable getFileTable() {return fileTable;}

    public Heap  getHeap() {return heap;}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Id = ").append(id).append("\n");


        sb.append("Execution Stack:\n");
        List<Statement> stackContent = executionStack.getStack(); // assuming this returns a List or similar
        for (int i = stackContent.size() - 1; i >= 0; i--) { // top to bottom
            sb.append(" ").append(stackContent.get(i).toString()).append("\n");
        }


        sb.append("Symbol Table:\n");
        for (var entry : symbolTable.getSymbolTable().entrySet()) {
            sb.append("   ").append(entry.getKey()).append(" --> ").append(entry.getValue()).append("\n");
        }

        sb.append("Heap:\n");
        for (var entry : heap.getContent().entrySet()) {
            sb.append("   ").append(entry.getKey())
                    .append(" --> ").append(entry.getValue())
                    .append("\n");
        }

        sb.append("Out:\n");
        out.getOut().forEach(val -> sb.append("   ").append(val).append("\n"));

        sb.append("FileTable:\n");
        sb.append("   (none)\n");
        return sb.toString();
    }

    public boolean isNotCompleted() {
        return !executionStack.isEmpty();
    }


    public ProgramState oneStep() {
        if (executionStack.isEmpty()) {
            throw new EmptyStackException("Program stack is empty!");
        }
        Statement crtStmt = executionStack.pop();
        return crtStmt.execute(this); // fork will return a new ProgramState; others return null
    }


}
