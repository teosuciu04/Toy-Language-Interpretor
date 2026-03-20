package model.statement;

import exceptions.InvalidTypeException;
import model.expression.ConstantExpression;
import model.state.ExecutionStack;
import model.state.MapTypeEnv;
import model.state.ProgramState;
import model.type.Type;
import model.value.IntValue;


public record WaitStatement(int number) implements Statement {
    public WaitStatement {
        if (number < 0) {
            throw new IllegalArgumentException("wait(number): number must be >= 0");
        }
    }

    @Override
    public ProgramState execute(ProgramState state) {
        if (number == 0) {
            return null;
        }

        ExecutionStack<Statement> stack = state.getExecutionStack();

        Statement printN = new PrintStatement(new ConstantExpression(new IntValue(number)));
        Statement afterWait = new WaitStatement(number - 1);
        stack.push(new CompoundStatement(printN, afterWait));

        return null;
    }

    @Override
    public MapTypeEnv<String, Type> typeCheck(MapTypeEnv<String, Type> typeEnv) throws InvalidTypeException {
        return typeEnv;
    }

    @Override
    public String toString() {
        return "wait(" + number + ")";
    }
}

