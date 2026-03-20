package model.statement;

import exceptions.InvalidTypeException;
import model.expression.Expression;
import model.state.MapTypeEnv;
import model.state.ProgramState;
import model.type.BoolType;
import model.type.IntType;
import model.type.Type;

public record ConditionalAssignmentStatement(String variableName, Expression exp1, Expression exp2, Expression exp3) implements Statement {
    @Override
    public ProgramState execute(ProgramState state) {
        Statement converted = new IfStatement(
                exp1,
                new AssignmentStatement(exp2,variableName),
                new AssignmentStatement(exp3,variableName)
        );

        state.getExecutionStack().push(converted);
        return null;
    }

    @Override
    public MapTypeEnv<String, Type> typeCheck(MapTypeEnv<String, Type> typeEnv) throws InvalidTypeException {
        Type t1 = exp1.typeCheck(typeEnv);
        Type t2 = exp2.typeCheck(typeEnv);
        Type t3 = exp3.typeCheck(typeEnv);
        Type typeVar = typeEnv.get(variableName);


        if (!t1.equals(new BoolType()))
            throw new InvalidTypeException("Type mismatch");

        if (typeVar.equals(t2) &&  typeVar.equals(t3))
            return typeEnv;
        else throw new InvalidTypeException("Type mismatch");
    }
    @Override
    public String toString() {
        return variableName + " = " + exp1 + " ? " + exp2 + " : " + exp3;
    }
}

