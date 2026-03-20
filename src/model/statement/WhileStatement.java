package model.statement;

import exceptions.InvalidTypeException;
import model.expression.Expression;
import model.state.ExecutionStack;
import model.state.MapTypeEnv;
import model.state.ProgramState;
import model.type.BoolType;
import model.type.Type;
import model.value.BooleanValue;
import model.value.Value;

public record WhileStatement(Expression condition, Statement body) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) {
        var symbolTable =  state.getSymbolTable();
        var heap = state.getHeap();
        ExecutionStack<Statement> stack = state.getExecutionStack();

        Value conditionValue = condition.evaluate(symbolTable,heap);
        if (!(conditionValue instanceof BooleanValue booleanValue)) {
            throw new InvalidTypeException("Consider a boolean value");
        }

        if (booleanValue.getValue()) {
            stack.push(this);
            stack.push(body);
        }

        return null;
    }

    @Override
    public MapTypeEnv<String, Type> typeCheck(MapTypeEnv<String, Type> typeEnv) throws InvalidTypeException {
        Type condType = condition.typeCheck(typeEnv);
        if (!condType.equals(new BoolType())){
            throw new InvalidTypeException("Consider a boolean value");
        }

        body.typeCheck((MapTypeEnv<String, Type>) typeEnv.clone());
        return typeEnv;
    }


    @Override
    public String toString() {
        return "while (" + condition + ") " + body;
    }
}
