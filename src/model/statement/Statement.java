package model.statement;

import exceptions.InvalidTypeException;
import exceptions.UndefinedVariableException;
import model.state.MapTypeEnv;
import model.state.ProgramState;
import model.type.Type;

public interface Statement {
    ProgramState execute(ProgramState state);
    MapTypeEnv<String, Type> typeCheck(MapTypeEnv<String, Type> typeEnv) throws InvalidTypeException;
}
