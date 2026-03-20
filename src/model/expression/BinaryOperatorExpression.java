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

public record BinaryOperatorExpression(String operator, Expression left, Expression right) implements Expression {
    @Override
    public Value evaluate(SymbolTable<String, Value> symTable, Heap heap) {
        var leftTerm = left.evaluate(symTable, heap);
        var rightTerm = right.evaluate(symTable, heap);

        switch (operator) {
            case "+", "-", "*", "/":
                checkTypes(leftTerm, rightTerm, new IntType());
                var leftValue = (IntValue) leftTerm;
                var rightValue = (IntValue) rightTerm;
                return evaluateArithmeticExpression(leftValue, rightValue);
            case "&&", "||":
                checkTypes(leftTerm, rightTerm, new BoolType());
                var leftValueBool = (BooleanValue) leftTerm;
                var rightValueBool = (BooleanValue) rightTerm;
                return evaluateBooleanExpression(leftValueBool, rightValueBool);

        }
        throw new IllegalArgumentException("Unknown operator: " + operator);
    }

    @Override
    public Type typeCheck(MapTypeEnv<String, Type> typeEnv) throws InvalidTypeException {

        Type leftType = left.typeCheck(typeEnv);
        Type rightType = right.typeCheck(typeEnv);

        switch (operator) {
            // arithmetic operators
            case "+", "-", "*", "/":
                if (leftType.equals(new IntType()) && rightType.equals(new IntType()))
                    return new IntType();
                throw new InvalidTypeException("Arithmetic operator '" + operator +
                        "' requires int operands");

                // relational operators (result is boolean)
            case "<", "<=", "==", "!=", ">", ">=":
                if (leftType.equals(new IntType()) && rightType.equals(new IntType()))
                    return new BoolType();
                throw new InvalidTypeException("Relational operator '" + operator +
                        "' requires int operands");

            default:
                throw new InvalidTypeException("Unknown operator: " + operator);
        }
    }

    private void checkTypes(Value leftTerm, Value rightTerm, Type type) {
        if (!leftTerm.getType().equals(type) || !rightTerm.getType().equals(type))
        {
            throw new RuntimeException("Wrong types");
        }
    }

    private IntValue evaluateArithmeticExpression(IntValue left, IntValue right) {
        return switch(operator){
            case "+" -> new IntValue(left.value() + right.value());
            case "-" -> new IntValue(left.value() - right.value());
            case "*" -> new IntValue(left.value() * right.value());
            case "/" -> new IntValue(left.value() / right.value());
            default -> throw new IllegalStateException("Unreachable code");
        };
    }

    private BooleanValue evaluateBooleanExpression(BooleanValue left, BooleanValue right){
        return switch(operator){
            case "&&" -> new BooleanValue(left.value() && right.value());
            case "||" -> new BooleanValue(left.value() || right.value());
            default -> throw new IllegalStateException("Unreachable code");
        };
    }

    @Override
    public String toString() {
        return left.toString() + " " + operator + " " + right.toString();
    }

}
