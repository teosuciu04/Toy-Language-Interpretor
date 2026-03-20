package model.statement;

import exceptions.InvalidTypeException;
import model.expression.EqualsExpression;
import model.expression.Expression;
import model.state.MapTypeEnv;
import model.state.ProgramState;
import model.type.Type;

public record SwitchStatement(Expression exp,
        Expression exp1, Statement stmt1,
        Expression exp2, Statement stmt2,
        Statement stmt3
) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) {
        Statement converted =
                new IfStatement(
                        new EqualsExpression(exp, exp1),
                        stmt1,
                        new IfStatement(
                                new EqualsExpression(exp, exp2),
                                stmt2,
                                stmt3
                        )
                );

        state.getExecutionStack().push(converted);
        return null;
    }

    @Override
    public MapTypeEnv<String, Type> typeCheck(MapTypeEnv<String, Type> typeEnv) throws InvalidTypeException {
        Type t = exp.typeCheck(typeEnv);
        Type t1 = exp1.typeCheck(typeEnv);
        Type t2 = exp2.typeCheck(typeEnv);

        if (!t.equals(t1) || !t.equals(t2)) {
            throw new InvalidTypeException("switch requires exp, exp1, exp2 to have the same type");
        }

        stmt1.typeCheck((MapTypeEnv<String, Type>) typeEnv.clone());
        stmt2.typeCheck((MapTypeEnv<String, Type>) typeEnv.clone());
        stmt3.typeCheck((MapTypeEnv<String, Type>) typeEnv.clone());

        return typeEnv;
    }

    @Override
    public String toString() {
        return "switch(" + exp + ") "
                + "(case " + exp1 + ": " + stmt1 + ") "
                + "(case " + exp2 + ": " + stmt2 + ") "
                + "(default: " + stmt3 + ")";
    }
}

