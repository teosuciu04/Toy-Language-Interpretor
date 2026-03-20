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

public record CloseRFileStatement(Expression exp) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) {

        Value v = exp.evaluate(state.getSymbolTable(), state.getHeap());
        if (!v.getType().equals(new StringType()))
            throw new RuntimeException("closeRFile: expression is not a string.");

        StringValue fileName = (StringValue) v;

        FileTable fileTable = state.getFileTable();

        if (!fileTable.isDefined(fileName))
            throw new RuntimeException("closeRFile: file not opened.");

        try {
            fileTable.lookup(fileName).close();   // Close BufferedReader
            fileTable.remove(fileName);           // Remove from FileTable

        } catch (Exception e) {
            throw new RuntimeException("closeRFile: could not close file.");
        }

        return null;
    }

    @Override
    public MapTypeEnv<String, Type> typeCheck(MapTypeEnv<String, Type> typeEnv) throws InvalidTypeException {
        Type type = exp.typeCheck(typeEnv);
        if(!type.equals(new StringType()))
            throw new InvalidTypeException("closeRFile: expression is not a string.");
        return typeEnv;
    }

    @Override
    public String toString() {
        return "closeRFile(" + exp + ")";
    }
}
