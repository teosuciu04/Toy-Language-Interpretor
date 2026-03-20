package model.expression;

import exceptions.InvalidTypeException;
import model.state.Heap;
import model.state.MapTypeEnv;
import model.state.SymbolTable;
import model.type.BoolType;
import model.type.Type;
import model.value.BooleanValue;
import model.value.Value;

public record NotExpression(Expression expr) implements Expression {

    @Override
    public Value evaluate(SymbolTable<String, Value> symTable, Heap heap) {
        Value v = expr.evaluate(symTable, heap);
        if (!(v instanceof BooleanValue b)) {
            throw new InvalidTypeException("Consider a boolean value");
        }
        return new BooleanValue(!b.value());
    }

    @Override
    public Type typeCheck(MapTypeEnv<String, Type> typeEnv) throws InvalidTypeException {
        Type t = expr.typeCheck(typeEnv);
        if (!t.equals(new BoolType())) {
            throw new InvalidTypeException("Consider a boolean value");
        }
        return new BoolType();
    }

    @Override
    public String toString() {
        return "!" + expr;
    }
}
