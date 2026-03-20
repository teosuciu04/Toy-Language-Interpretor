package view.gui;

import controller.Controller;
import model.statement.Statement;

public class ExampleProgram {
    private final String name;
    private final Statement statement;
    private final Controller controller;

    public ExampleProgram(String name, Statement statement, Controller controller) {
        this.name = name;
        this.statement = statement;
        this.controller = controller;
    }

    public String getName() {
        return name;
    }

    public Statement getStatement() {
        return statement;
    }

    public Controller getController() {
        return controller;
    }

    @Override
    public String toString() {
        // ListView item text: "string representation of a possible program (IStmt)"
        return statement.toString();
    }
}
