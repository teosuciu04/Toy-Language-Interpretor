package model.statement;

import exceptions.InvalidTypeException;
import exceptions.UndefinedVariableException;
import model.expression.Expression;
import model.state.Heap;
import model.state.MapTypeEnv;
import model.state.ProgramState;
import model.state.SymbolTable;
import model.type.RefType;
import model.type.Type;
import model.value.RefValue;
import model.value.Value;

public record WriteHeapStatement(String variableName, Expression expression) implements Statement {
    @Override
    public ProgramState execute(ProgramState state) {
        SymbolTable<String, Value> symbolTable = state.getSymbolTable();
        Heap heap = state.getHeap();

        if (!symbolTable.isDefined(variableName)) {
            throw new UndefinedVariableException("wH: variable " + variableName + " is not defined");
        }

        Value value = symbolTable.getValue(variableName);

        if (!(value.getType() instanceof RefType refType))
            throw new InvalidTypeException("wH: variable " + variableName + " is not a ref type");

        RefValue refValue = (RefValue) value;
        int address = refValue.getAddress();

        if (!heap.isDefined(address))
            throw new RuntimeException("wH: address " + address + " not in heap");

        Value newValue = expression.evaluate(symbolTable, heap);
        if (!newValue.getType().equals(refType.getInner()))
            throw new InvalidTypeException("wH: type mismatch, expected " +
                    refType.getInner() + " but got " + newValue.getType());

        heap.write(address, newValue);

        return null;

    }

    @Override
    public MapTypeEnv<String, Type> typeCheck(MapTypeEnv<String, Type> typeEnv) throws InvalidTypeException {

        Type varType = typeEnv.get(variableName);

        if (!(varType instanceof RefType refType))
            throw new InvalidTypeException("wH: variable not RefType");

        Type expType = expression.typeCheck(typeEnv);

        if (!refType.getInner().equals(expType))
            throw new InvalidTypeException("wH: type mismatch");

        return typeEnv;
    }


    @Override
    public String toString() {
        return "wH(" + variableName + ", " + expression + ")";
    }
}
