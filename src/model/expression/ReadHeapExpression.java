package model.expression;

import exceptions.InvalidTypeException;
import model.state.Heap;
import model.state.MapTypeEnv;
import model.state.SymbolTable;
import model.type.RefType;
import model.type.Type;
import model.value.RefValue;
import model.value.Value;

public record ReadHeapExpression(Expression expression) implements Expression {
    @Override
    public Value evaluate(SymbolTable<String, Value> symTable, Heap heap) {
        Value value = expression.evaluate(symTable, heap);

        if (!(value instanceof RefValue refValue))
            throw new InvalidTypeException("rH: expression is not a RefValue");

        int address = refValue.getAddress();

        if (!heap.isDefined(address))
            throw new RuntimeException("rH: address is not defined");

        return heap.read(address);
    }

    @Override
    public Type typeCheck(MapTypeEnv<String, Type> typeEnv) throws InvalidTypeException {
        Type type = expression.typeCheck(typeEnv);
        if (type instanceof RefType refType)
            return refType.getInner();
        throw new InvalidTypeException("rH: type is not a RefType");
    }

    @Override
    public String toString() {
        return "rH(" + expression + ")";
    }
}
