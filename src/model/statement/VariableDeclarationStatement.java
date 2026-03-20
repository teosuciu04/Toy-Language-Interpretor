package model.statement;

import exceptions.InvalidTypeException;
import exceptions.VariableAlreadyDefinedException;
import model.state.MapTypeEnv;
import model.state.ProgramState;
import model.type.Type;
import model.value.Value;

public record VariableDeclarationStatement(Type type, String variableName) implements Statement {
    @Override
    public ProgramState execute(ProgramState state) {
        var symbolTable = state.getSymbolTable();
        if(symbolTable.isDefined(variableName)) {
            throw new VariableAlreadyDefinedException("Variable " + variableName + " is already defined");
        }
        Value defaultValue = type.defaultValue();
        symbolTable.put(variableName, defaultValue);
        return null;
    }

    @Override
    public MapTypeEnv<String, Type> typeCheck(MapTypeEnv<String, Type> typeEnv) throws InvalidTypeException {
        typeEnv.put(variableName,type);
        return typeEnv;
    }

    @Override
    public String toString() {
        return type.toString().toLowerCase() + " " + variableName;
    }
}
