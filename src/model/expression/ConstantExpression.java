package model.expression;

import exceptions.InvalidTypeException;
import model.state.Heap;
import model.state.MapTypeEnv;
import model.state.SymbolTable;
import model.type.Type;
import model.value.Value;

public record ConstantExpression(Value value) implements Expression {
    @Override
    public Value evaluate(SymbolTable<String, Value> symTable, Heap heap) {
        return value;
    }

    @Override
    public Type typeCheck(MapTypeEnv<String, Type> typeEnv) throws InvalidTypeException {
        return value.getType();
    }

    @Override
    public String toString(){
        return value.toString();
    }
}
