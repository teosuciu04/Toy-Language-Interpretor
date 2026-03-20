package model.statement;

import exceptions.InvalidTypeException;
import exceptions.UndefinedVariableException;
import exceptions.VariableAlreadyDefinedException;
import model.expression.Expression;
import model.state.Heap;
import model.state.MapTypeEnv;
import model.state.ProgramState;
import model.state.SymbolTable;
import model.type.RefType;
import model.type.Type;
import model.value.IntValue;
import model.value.RefValue;
import model.value.Value;

public record AssignmentStatement(Expression expression, String variableName) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) {
        SymbolTable<String, Value> symbolTable = state.getSymbolTable();
        Heap heap = state.getHeap();

        if(!symbolTable.isDefined(variableName)) {
            throw new UndefinedVariableException(variableName + "Undefined");
        }

        Value value = expression.evaluate(symbolTable,heap);
        Type valueType = symbolTable.getType(variableName);

        if (valueType instanceof RefType refType) {
            if (value instanceof IntValue intVal && intVal.value() == 0) {
                // assign null reference
                symbolTable.update(variableName, new RefValue(0, refType.getInner()));
                return null;
            }
        }

        if(!value.getType().equals(valueType)){
            throw new InvalidTypeException("Type mismatch");
        }
        symbolTable.update(variableName, value);
        return null;
    }

    @Override
    public MapTypeEnv<String, Type> typeCheck(MapTypeEnv<String, Type> typeEnv) throws InvalidTypeException {
        Type varType = typeEnv.get(variableName);
        Type exprType = expression.typeCheck(typeEnv);

        if (!varType.equals(exprType)) {
            throw new InvalidTypeException("Type mismatch");
        }

        return typeEnv;
    }

    @Override
    public String toString() {
        return variableName + " = " + expression;
    }
}
