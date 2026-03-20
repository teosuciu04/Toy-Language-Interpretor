package model.expression;

import exceptions.InvalidTypeException;
import model.state.Heap;
import model.state.MapTypeEnv;
import model.state.SymbolTable;
import model.type.Type;
import model.value.Value;

public interface Expression {
    Value evaluate(SymbolTable<String, Value> symTable, Heap heap);
    Type typeCheck(MapTypeEnv<String,Type> typeEnv) throws InvalidTypeException;
}
