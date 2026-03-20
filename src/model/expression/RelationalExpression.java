package model.expression;

import exceptions.InvalidTypeException;
import model.state.Heap;
import model.state.MapTypeEnv;
import model.state.SymbolTable;
import model.type.BoolType;
import model.type.IntType;
import model.type.Type;
import model.value.BooleanValue;
import model.value.IntValue;
import model.value.Value;

public record RelationalExpression(String operator, Expression left, Expression right) implements Expression {
    @Override
    public Value evaluate(SymbolTable<String, Value> symTable, Heap heap) {
        Value v1 = left.evaluate(symTable,heap);
        Value v2 = right.evaluate(symTable,heap);

        if (!v1.getType().equals(new IntType()))
            throw new RuntimeException("Left operand is not an integer");

        if (!v2.getType().equals(new IntType()))
            throw new RuntimeException("Right operand is not an integer");

        int n1 = ((IntValue) v1).value();
        int n2 = ((IntValue) v2).value();

        return switch (operator){
            case "<" -> new BooleanValue(n1<n2);
            case "<=" -> new BooleanValue(n2<=n1);
            case ">" -> new BooleanValue(n1>n2);
            case ">=" -> new BooleanValue(n2>=n1);
            case "==" -> new BooleanValue(n1==n2);
            case "!=" -> new BooleanValue(n1!=n2);
            default -> throw new RuntimeException("Wrong types");
        };
    }

    @Override
    public Type typeCheck(MapTypeEnv<String, Type> typeEnv) throws InvalidTypeException {
        Type leftType = left.typeCheck(typeEnv);
        Type rightType = right.typeCheck(typeEnv);
        if (leftType.equals(new IntType()) && rightType.equals(new IntType()))
            return new BoolType();
        throw new InvalidTypeException("Int operator '" + operator +
                "' requires int operands");

    }

    @Override
    public String toString(){
        return "(" + left.toString() + ", " + right.toString() + ")";
    }
}
