package model.statement;

import exceptions.InvalidTypeException;
import model.expression.Expression;
import model.state.MapTypeEnv;
import model.state.ProgramState;
import model.type.BoolType;
import model.type.Type;
import model.value.BooleanValue;
import model.value.Value;

public record IfStatement(Expression condition, Statement thenBranch, Statement elseBranch) implements Statement{

    @Override
    public ProgramState execute(ProgramState state) {
        Value result = condition.evaluate(state.getSymbolTable(), state.getHeap());
        if (result instanceof BooleanValue booleanValue) {
            if (booleanValue.getValue()) {
                state.getExecutionStack().push(thenBranch);
            } else {
                state.getExecutionStack().push(elseBranch);
            }
        } else {
            throw new RuntimeException("Condition expression does not evaluate to a boolean.");
        }
        return null;
    }

    @Override
    public MapTypeEnv<String, Type> typeCheck(MapTypeEnv<String, Type> typeEnv) throws InvalidTypeException {
        Type exprType = condition.typeCheck(typeEnv);
        if (!exprType.equals(new BoolType()))
            throw new InvalidTypeException("Condition expression does not evaluate to a boolean.");

        thenBranch.typeCheck((MapTypeEnv<String, Type>) typeEnv.clone());
        elseBranch.typeCheck((MapTypeEnv<String, Type>) typeEnv.clone());

        return typeEnv;
    }

    @Override
    public String toString() {
        return "if (" + condition + ") then (" + thenBranch + ") else (" + elseBranch + ")";
    }

}
