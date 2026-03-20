package model.statement;

import exceptions.InvalidTypeException;
import model.expression.Expression;
import model.expression.NotExpression;
import model.state.ExecutionStack;
import model.state.MapTypeEnv;
import model.state.ProgramState;
import model.type.BoolType;
import model.type.Type;


public record RepeatUntilStatement(Statement body, Expression condition) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) {
        ExecutionStack<Statement> stack = state.getExecutionStack();

        Statement converted = new CompoundStatement(
                body,
                new WhileStatement(new NotExpression(condition), body)
        );

        stack.push(converted);
        return null;
    }

    @Override
    public MapTypeEnv<String, Type> typeCheck(MapTypeEnv<String, Type> typeEnv) throws InvalidTypeException {
        Type condType = condition.typeCheck(typeEnv);
        if (!condType.equals(new BoolType())) {
            throw new InvalidTypeException("Consider a boolean value");
        }

        body.typeCheck((MapTypeEnv<String, Type>) typeEnv.clone());
        return typeEnv;
    }

    @Override
    public String toString() {
        return "repeat " + body + " until " + condition;
    }
}
