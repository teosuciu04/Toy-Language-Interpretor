package model.statement;

import exceptions.InvalidTypeException;
import model.state.MapTypeEnv;
import model.state.ProgramState;
import model.type.Type;

public record SleepStatement(int number) implements Statement{
    @Override
    public ProgramState execute(ProgramState state) {
       if (number > 0){
           state.getExecutionStack().push(new SleepStatement(number-1));
       }
       return null;
    }

    @Override
    public MapTypeEnv<String, Type> typeCheck(MapTypeEnv<String, Type> typeEnv) throws InvalidTypeException {
        return typeEnv;
    }

    @Override
    public String toString() {
        return "sleep(" + number + ")";
    }
}
