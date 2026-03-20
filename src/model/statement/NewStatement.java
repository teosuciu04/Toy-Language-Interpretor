package model.statement;

import exceptions.InvalidTypeException;
import exceptions.UndefinedVariableException;
import model.expression.Expression;
import model.state.Heap;
import model.state.MapTypeEnv;
import model.state.ProgramState;
import model.type.RefType;
import model.type.Type;
import model.value.RefValue;
import model.value.Value;

public record NewStatement(String variableName, Expression expression) implements Statement {
    @Override
    public ProgramState execute(ProgramState state) {
        var symboltable = state.getSymbolTable();
        Heap heap = state.getHeap();

        if (!symboltable.isDefined(variableName)) {
            throw new UndefinedVariableException(variableName);
        }

        Value value = symboltable.getValue(variableName);
        if (!(value.getType() instanceof RefType refType))
            throw new InvalidTypeException("new: variable " + variableName + " is not of RefType");

        Value evalValue = expression.evaluate(symboltable,heap);
        if (!(evalValue.getType().equals(refType.getInner())))
            throw new InvalidTypeException("new: type mismatch, expected " +
                    refType.getInner() + " but got " + evalValue.getType());

        int address = heap.allocate(evalValue);
        symboltable.update(variableName,new RefValue(address, refType.getInner()));

        return null;
    }

    @Override
    public MapTypeEnv<String, Type> typeCheck(MapTypeEnv<String, Type> typeEnv) throws InvalidTypeException {
        Type varType = typeEnv.get(variableName);
        Type exprType = expression.typeCheck(typeEnv);

        if (!varType.equals(new RefType(exprType)))
            throw new InvalidTypeException("new: variable " + variableName + " is not of RefType");

        return typeEnv;
    }

    @Override
    public String toString() {
        return "new(" + variableName + ", " + expression + ")";
    }
}
