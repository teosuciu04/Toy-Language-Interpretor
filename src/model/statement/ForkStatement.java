package model.statement;

import exceptions.InvalidTypeException;
import model.state.MapTypeEnv;
import model.state.ProgramState;
import model.state.StackExecutionStack;
import model.state.SymbolTable;
import model.type.Type;
import model.value.Value;

public class ForkStatement implements Statement {
    private final Statement forkBody;

    public ForkStatement(Statement forkBody) {
        this.forkBody = forkBody;
    }

    @Override
    public ProgramState execute(ProgramState state) {
        // new execution stack for the child
        StackExecutionStack<Statement> newStack = new StackExecutionStack<>();
        newStack.push(forkBody);

        // deep copy of symbol table
        SymbolTable<String, Value> newSymTable = state.getSymbolTable().deepCopy();

        return new ProgramState(
                newStack,
                newSymTable,
                state.getOut(),
                state.getFileTable(),
                forkBody,     // kept only for display / originalProgram field
                state.getHeap()
        );
    }


    @Override
    public MapTypeEnv<String, Type> typeCheck(MapTypeEnv<String, Type> typeEnv) throws InvalidTypeException {
        forkBody.typeCheck((MapTypeEnv<String, Type>) typeEnv.clone());
        return typeEnv;
    }

    @Override
    public String toString() {
        return "fork(" + forkBody + ")";
    }
}
