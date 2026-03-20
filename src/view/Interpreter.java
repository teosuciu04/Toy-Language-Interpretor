package view;

import controller.Controller;
import model.expression.*;
import model.state.*;
import model.statement.*;
import model.type.BoolType;
import model.type.IntType;
import model.type.RefType;
import model.type.StringType;
import model.value.BooleanValue;
import model.value.IntValue;
import model.value.StringValue;
import repository.ArrayRepository;
import repository.Repository;

public class Interpreter {
    private static Statement getEx1(){
        return new CompoundStatement(
                new VariableDeclarationStatement(new IntType(), "v"),
                new CompoundStatement(
                        new AssignmentStatement(new ConstantExpression(new StringValue("egreg")), "v"),
                        new PrintStatement(new VariableExpression("v"))

                )
        );
    }

    private static Statement getEx2(){
        return new CompoundStatement(
                new VariableDeclarationStatement(new IntType(), "a"),
                new CompoundStatement(
                        new VariableDeclarationStatement(new IntType(), "b"),
                        new CompoundStatement(
                                new AssignmentStatement(
                                        new BinaryOperatorExpression("+",
                                                new ConstantExpression(new IntValue(2)),
                                                new BinaryOperatorExpression("*",new ConstantExpression(new IntValue(3)), new ConstantExpression(new IntValue(5)))),"a"),
                                new CompoundStatement(
                                        new AssignmentStatement(new BinaryOperatorExpression("+", new VariableExpression("a"), new ConstantExpression(new IntValue(1))),"b"),
                                        new PrintStatement(new VariableExpression("b"))
                                )
                        )
                )
        );
    }

    private static Statement getEx3(){
        return new CompoundStatement(
                new VariableDeclarationStatement(new BoolType(), "a"),
                new CompoundStatement(
                        new VariableDeclarationStatement(new IntType(), "v"),
                        new CompoundStatement(
                                new AssignmentStatement(new ConstantExpression(new BooleanValue(true)),"a"),
                                new CompoundStatement(
                                        new IfStatement(
                                                new VariableExpression("a"),
                                                new AssignmentStatement(new ConstantExpression(new IntValue(2)),"v"),
                                                new AssignmentStatement(new ConstantExpression(new IntValue(3)),"v")
                                        ),
                                        new PrintStatement(new VariableExpression("v"))
                                )
                        )
                )
        );
    }

    private static Statement getFileExample() {
        return new CompoundStatement(
                new VariableDeclarationStatement(new StringType(), "varf"),
                new CompoundStatement(
                        new AssignmentStatement(
                                new ConstantExpression(new StringValue("test.in")),
                                "varf"
                        ),
                        new CompoundStatement(
                                new OpenRFileStatement(new VariableExpression("varf")),
                                new CompoundStatement(
                                        new VariableDeclarationStatement(new IntType(), "varc"),
                                        new CompoundStatement(
                                                new ReadFileStatement(new VariableExpression("varf"), "varc"),
                                                new CompoundStatement(
                                                        new PrintStatement(new VariableExpression("varc")),
                                                        new CompoundStatement(
                                                                new ReadFileStatement(new VariableExpression("varf"), "varc"),
                                                                new CompoundStatement(
                                                                        new PrintStatement(new VariableExpression("varc")),
                                                                        new CloseRFileStatement(new VariableExpression("varf"))
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
    }

    private static Statement getEx4(){
        return new CompoundStatement(
                new VariableDeclarationStatement(new RefType(new IntType()),"v"),
                new CompoundStatement(
                        new NewStatement("v", new ConstantExpression(new IntValue(20))),
                        new CompoundStatement(
                                new PrintStatement(new ReadHeapExpression(new VariableExpression("v"))),
                                new CompoundStatement(
                                        new WriteHeapStatement("v", new ConstantExpression(new IntValue(30))),
                                        new PrintStatement(new BinaryOperatorExpression("+",new ReadHeapExpression(new VariableExpression("v")),new ConstantExpression(new IntValue(5))))
                                )
                        )
                )

        );

    }
    private static Statement getGCExample() {
        return new CompoundStatement(
                // Ref int v;
                new VariableDeclarationStatement(new RefType(new IntType()), "v"),
                new CompoundStatement(
                        // new(v,20);
                        new NewStatement("v", new ConstantExpression(new IntValue(20))),
                        new CompoundStatement(
                                // Ref int a;
                                new VariableDeclarationStatement(new RefType(new IntType()), "a"),
                                new CompoundStatement(
                                        // new(a, 30);
                                        new NewStatement("a", new ConstantExpression(new IntValue(30))),
                                        // v = 0;   ← this removes last reference to address 1
                                        new AssignmentStatement(
                                                new ConstantExpression(new IntValue(0)), "v"
                                        )
                                )
                        )
                )
        );
    }

    private static Statement getEx6() {
        Expression cond = new RelationalExpression(
                ">", new VariableExpression("v"), new ConstantExpression(new IntValue(0))
        );

        Statement body = new CompoundStatement(
                new PrintStatement(new VariableExpression("v")),
                new AssignmentStatement(
                        new BinaryOperatorExpression(
                                "-", new VariableExpression("v"),
                                new ConstantExpression(new IntValue(1))
                        ),
                        "v"
                )
        );

        return new CompoundStatement(
                new VariableDeclarationStatement(new IntType(), "v"),
                new CompoundStatement(
                        new AssignmentStatement(new ConstantExpression(new IntValue(4)), "v"),
                        new CompoundStatement(
                                new WhileStatement(cond, body),
                                new PrintStatement(new VariableExpression("v"))
                        )
                )
        );


    }

    private static Statement getForkExample() {
        return new CompoundStatement(
                // int v;
                new VariableDeclarationStatement(new IntType(), "v"),
                new CompoundStatement(
                        // Ref int a;
                        new VariableDeclarationStatement(new RefType(new IntType()), "a"),
                        new CompoundStatement(
                                // v = 10;
                                new AssignmentStatement(
                                        new ConstantExpression(new IntValue(10)), "v"
                                ),
                                new CompoundStatement(
                                        // new(a, 22);
                                        new NewStatement(
                                                "a",
                                                new ConstantExpression(new IntValue(22))
                                        ),
                                        new CompoundStatement(
                                                // fork( ... )
                                                new ForkStatement(
                                                        new CompoundStatement(
                                                                new WriteHeapStatement(
                                                                        "a",
                                                                        new ConstantExpression(new IntValue(30))
                                                                ),
                                                                new CompoundStatement(
                                                                        new AssignmentStatement(
                                                                                new ConstantExpression(new IntValue(32)), "v"
                                                                        ),
                                                                        new CompoundStatement(
                                                                                new PrintStatement(
                                                                                        new VariableExpression("v")
                                                                                ),
                                                                                new PrintStatement(
                                                                                        new ReadHeapExpression(
                                                                                                new VariableExpression("a")
                                                                                        )
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                ),
                                                // parent continues here
                                                new CompoundStatement(
                                                        new PrintStatement(
                                                                new VariableExpression("v")
                                                        ),
                                                        new PrintStatement(
                                                                new ReadHeapExpression(
                                                                        new VariableExpression("a")
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
    }



    private static ProgramState makeProgram(Statement stmt) {

        StackExecutionStack<Statement> stack = new StackExecutionStack<>();
        stack.push(stmt);

        return new ProgramState(
                stack,
                new MapSymbolTable<>(),
                new ListOut<>(),
                new MapFileTable(),
                stmt,
                new MapHeap()
        );

    }

    public static void main(String[] args) {
        Statement ex1 = getEx1();
        Statement ex2 = getEx2();
        Statement ex3 = getEx3();
        Statement ex4 = getEx4();
        Statement ex5 = getGCExample();
        Statement ex6 = getEx6();
        Statement exFork = getForkExample();

        ProgramState prg1 = makeProgram(ex1);
        ProgramState prg2 = makeProgram(ex2);
        ProgramState prg3 = makeProgram(ex3);
        ProgramState prg4 = makeProgram(ex4);
        ProgramState prg5 = makeProgram(ex5);
        ProgramState prg6 = makeProgram(ex6);
        ProgramState prgFork = makeProgram(exFork);

        Repository repo1 = new ArrayRepository(prg1, "log1.txt");
        Repository repo2 = new ArrayRepository(prg2, "log2.txt");
        Repository repo3 = new ArrayRepository(prg3, "log3.txt");
        Repository repo4 = new ArrayRepository(prg4, "log4.txt");
        Repository repo5 = new ArrayRepository(prg5, "log5.txt");
        Repository repo6 = new ArrayRepository(prg6, "log6.txt");
        Repository repoFork = new ArrayRepository(prgFork, "log7.txt");

        Controller ctrl1 = new Controller(repo1, true);
        Controller ctrl2 = new Controller(repo2, true);
        Controller ctrl3 = new Controller(repo3, true);
        Controller ctrl4 = new Controller(repo4, true);
        Controller ctrl5 = new Controller(repo5, true);
        Controller ctrl6 = new Controller(repo6, true);
        Controller ctrlFork = new Controller(repoFork, true);

        Statement fileEx = getFileExample();
        ProgramState prgFile = makeProgram(fileEx);
        Repository repoFile = new ArrayRepository(prgFile, "logFile.txt");
        Controller ctrlFile = new Controller(repoFile, true);

        TextMenu menu = new TextMenu();
        menu.addCommand(new ExitCommand("0", "exit"));
        menu.addCommand(new RunExample("1",ex1.toString(),ex1, ctrl1));
        menu.addCommand(new RunExample("2",ex2.toString(),ex2, ctrl2));
        menu.addCommand(new RunExample("3",ex3.toString(),ex3, ctrl3));
        menu.addCommand(new RunExample("4",ex4.toString(),ex4, ctrl4));
        menu.addCommand(new RunExample("5",ex5.toString(),ex5, ctrl5));
        menu.addCommand(new RunExample("6",ex6.toString(),ex6, ctrl6));
        menu.addCommand(new RunExample("7",fileEx.toString(),fileEx,ctrlFile));
        menu.addCommand(new RunExample("8", exFork.toString(), exFork, ctrlFork));
        menu.show();
    }

}
