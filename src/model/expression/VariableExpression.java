package model.expression;

import exceptions.InvalidTypeException;
import model.state.Heap;
import model.state.MapTypeEnv;
import model.state.SymbolTable;
import model.type.Type;
import model.value.Value;

public record VariableExpression(String variableName) implements Expression {
    @Override
    public Value evaluate(SymbolTable<String, Value> symTable, Heap heap) {
        if (!symTable.isDefined(variableName)) {
            throw new RuntimeException("Variable " + variableName + " is not defined");

        }
        return symTable.getValue(variableName);
    }

    @Override
    public Type typeCheck(MapTypeEnv<String, Type> typeEnv) throws InvalidTypeException {
        if (!typeEnv.isDefined(variableName)) {
            throw new InvalidTypeException("Variable " + variableName + " is not defined");
        }
        return typeEnv.get(variableName);
    }

    @Override
    public String toString() {
        return variableName;
    }
}
