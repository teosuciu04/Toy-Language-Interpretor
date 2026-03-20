package model.statement;

import exceptions.InvalidTypeException;
import model.state.MapTypeEnv;
import model.state.ProgramState;
import model.type.Type;

public class NoOperationStatement implements Statement {

    @Override
    public ProgramState execute(ProgramState state) {
        return null;
    }

    @Override
    public MapTypeEnv<String, Type> typeCheck(MapTypeEnv<String, Type> typeEnv) throws InvalidTypeException {
        return typeEnv;
    }

    @Override
    public String toString(){
        return "nop";
    }
}
