package model.statement;

import exceptions.InvalidTypeException;
import model.expression.Expression;
import model.expression.RelationalExpression;
import model.state.MapTypeEnv;
import model.state.ProgramState;
import model.type.Type;
import model.type.IntType;

public record ForStatement(String variableName, Expression exp1,  Expression exp2, Expression exp3, Statement stmt) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) {
        // As per the guide: for(v=exp1; v<exp2; v=exp3) stmt
        // transforms into: int v; v=exp1; while(v<exp2) { stmt; v=exp3; }

        Statement converted = new CompoundStatement(
                new VariableDeclarationStatement(new IntType(), variableName),
                new CompoundStatement(
                        new AssignmentStatement(exp1, variableName),
                        new WhileStatement(
                                new RelationalExpression("<", new model.expression.VariableExpression(variableName), exp2),
                                new CompoundStatement(stmt, new AssignmentStatement(exp3, variableName))
                        )
                )
        );

        state.getExecutionStack().push(converted);
        return null;
    }

    @Override
    public MapTypeEnv<String, Type> typeCheck(MapTypeEnv<String, Type> typeEnv) throws InvalidTypeException {
        // Verify exp1, exp2, and exp3 have type int
        Type t1 = exp1.typeCheck(typeEnv);
        Type t2 = exp2.typeCheck(typeEnv);
        Type t3 = exp3.typeCheck(typeEnv);

        if (t1.equals(new IntType()) && t2.equals(new IntType()) && t3.equals(new IntType())) {
            // Check the body with the variable defined in the environment
            MapTypeEnv<String, Type> nextEnv = (MapTypeEnv<String, Type>) typeEnv.clone();
            nextEnv.put(variableName, new IntType());
            stmt.typeCheck(nextEnv);
            return typeEnv;
        } else {
            throw new InvalidTypeException("For Statement: all expressions must be of type int.");
        }
    }

    @Override
    public String toString() {
        return "for(" + variableName + "=" + exp1 + "; " + variableName + "<" + exp2 + "; " + variableName + "=" + exp3 + ") " + stmt;
    }
}
