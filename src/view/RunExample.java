package view;

import controller.Controller;
import model.state.MapTypeEnv;
import model.statement.Statement;
import model.type.Type;

public class RunExample extends Command {

    private final Controller controller;
    private final Statement program;

    public RunExample(String key, String description, Statement program, Controller controller) {
        super(key, description);
        this.program = program;
        this.controller = controller;
    }
    public void execute() {
        try {
            // TYPECHECK BEFORE RUN
            MapTypeEnv<String, Type> typeEnv = new MapTypeEnv<>();
            program.typeCheck(typeEnv);

            // Only execute if typecheck succeeded
            controller.allStep();

        } catch (Exception e) {
            System.out.println("Typecheck or execution error: " + e.getMessage());
        }
    }
}
