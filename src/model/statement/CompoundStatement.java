package model.statement;

import exceptions.InvalidTypeException;
import model.state.ExecutionStack;
import model.state.MapTypeEnv;
import model.state.ProgramState;
import model.type.Type;

public record CompoundStatement(Statement first, Statement second) implements Statement{

    @Override
    public ProgramState execute(ProgramState state) {
        var stack = state.getExecutionStack();
        stack.push(second);
        stack.push(first);
        return null;
    }

    @Override
    public MapTypeEnv<String, Type> typeCheck(MapTypeEnv<String, Type> typeEnv) throws InvalidTypeException{
        return second.typeCheck(first.typeCheck(typeEnv));
    }

    @Override
    public String toString() {
        return first.toString() + ";\n     " + second.toString();
    }
}
