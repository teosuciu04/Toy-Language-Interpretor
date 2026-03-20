package model.statement;

import exceptions.InvalidTypeException;
import model.expression.Expression;
import model.state.FileTable;
import model.state.MapTypeEnv;
import model.state.ProgramState;
import model.type.StringType;
import model.type.Type;
import model.value.StringValue;
import model.value.Value;

import java.io.BufferedReader;
import java.io.FileReader;

public record OpenRFileStatement(Expression expression) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) {
        Value value = expression.evaluate(state.getSymbolTable(), state.getHeap());
        if(!value.getType().equals(new StringType())) {
            throw new RuntimeException("openRFileStatement only supports string types");
        }

        StringValue fileNameValue = (StringValue) value;

        FileTable fileTable = state.getFileTable();
        if(fileTable.isDefined(fileNameValue)) {
            throw new RuntimeException("openRFile: file already open.");
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileNameValue.getVal()));
            fileTable.add(fileNameValue, br);

        } catch (Exception e) {
            throw new RuntimeException("openRFile: could not open file " + fileNameValue.getVal());
        }
        return null;
    }

    @Override
    public MapTypeEnv<String, Type> typeCheck(MapTypeEnv<String, Type> typeEnv) throws InvalidTypeException {
        Type type = expression.typeCheck(typeEnv);
        if(!type.equals(new StringType())) {
            throw new InvalidTypeException("openRFileStatement only supports string types");
        }
        return typeEnv;
    }

    @Override
    public String toString() {
        return "openRFile(" + expression + ")";
    }
}
