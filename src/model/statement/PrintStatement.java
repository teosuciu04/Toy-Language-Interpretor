package model.statement;

import exceptions.InvalidTypeException;
import model.expression.Expression;
import model.state.MapTypeEnv;
import model.state.ProgramState;
import model.type.Type;
import model.value.Value;

public record PrintStatement(Expression expression) implements Statement {
    @Override
    public ProgramState execute(ProgramState state) {
        Value val = expression.evaluate(state.getSymbolTable(), state.getHeap());
        state.getOut().add(val);

        // IMPORTANT: actually print it
        System.out.println(val);

        return null;
    }

    @Override
    public MapTypeEnv<String, Type> typeCheck(MapTypeEnv<String, Type> typeEnv) throws InvalidTypeException {
        expression.typeCheck(typeEnv);
        return typeEnv;
    }

    @Override
    public String toString() {
        return "print(" + expression + ")";
    }
}
