package model.expression;

import exceptions.InvalidTypeException;
import model.state.Heap;
import model.state.MapTypeEnv;
import model.state.SymbolTable;
import model.type.BoolType;
import model.type.Type;
import model.value.BooleanValue;
import model.value.Value;

public record EqualsExpression(Expression left, Expression right) implements Expression {

    @Override
    public Value evaluate(SymbolTable<String, Value> symTable, Heap heap) {
        Value v1 = left.evaluate(symTable, heap);
        Value v2 = right.evaluate(symTable, heap);

        return new BooleanValue(v1.equals(v2));
    }

    @Override
    public Type typeCheck(MapTypeEnv<String, Type> typeEnv) throws InvalidTypeException {
        Type t1 = left.typeCheck(typeEnv);
        Type t2 = right.typeCheck(typeEnv);

        if (!t1.equals(t2)) {
            throw new InvalidTypeException("== requires operands of the same type, got " + t1 + " and " + t2);
        }
        return new BoolType();
    }

    @Override
    public String toString() {
        return "(" + left + " == " + right + ")";
    }
}

