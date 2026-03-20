package model.statement;

import exceptions.InvalidTypeException;
import model.expression.Expression;
import model.state.FileTable;
import model.state.MapTypeEnv;
import model.state.ProgramState;
import model.type.Type;
import model.value.IntValue;
import model.value.StringValue;
import model.type.StringType;
import model.type.IntType;
import model.value.Value;

import java.io.BufferedReader;

public record ReadFileStatement(Expression exp, String varName) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) {

        var symTable = state.getSymbolTable();
        var heap  = state.getHeap();

        // Ensure variable exists and is integer type
        if (!symTable.isDefined(varName))
            throw new RuntimeException("readFile: variable " + varName + " not declared.");

        if (!symTable.getType(varName).equals(new IntType()))
            throw new RuntimeException("readFile: variable " + varName + " is not int.");

        // Evaluate the file name expression
        Value v = exp.evaluate(symTable, heap);
        if (!v.getType().equals(new StringType()))
            throw new RuntimeException("readFile: expression is not a string.");

        StringValue fileName = (StringValue) v;

        // Get the BufferedReader from FileTable
        FileTable fileTable = state.getFileTable();
        if (!fileTable.isDefined(fileName))
            throw new RuntimeException("readFile: file not opened.");

        BufferedReader br = fileTable.lookup(fileName);

        try {
            String line = br.readLine();
            int value = (line == null ? 0 : Integer.parseInt(line.trim()));

            symTable.update(varName, new IntValue(value));

        } catch (Exception e) {
            throw new RuntimeException("readFile: error reading file.");
        }

        return null;
    }

    @Override
    public MapTypeEnv<String, Type> typeCheck(MapTypeEnv<String, Type> typeEnv) throws InvalidTypeException {
        Type exprType = exp.typeCheck(typeEnv);
        Type varType = typeEnv.get(varName);

        if (!exprType.equals(new StringType()) || !varType.equals(new StringType()))
            throw new InvalidTypeException("readFile: expression is not a string or variable is not an int");

        return typeEnv;
    }

    @Override
    public String toString() {
        return "readFile(" + exp + ", " + varName + ")";
    }
}
