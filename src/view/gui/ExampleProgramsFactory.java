package view.gui;

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

import java.util.ArrayList;
import java.util.List;

public final class ExampleProgramsFactory {
    private ExampleProgramsFactory() {}

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

    private static Statement ex1() {
        return new CompoundStatement(
                new VariableDeclarationStatement(new StringType(), "v"),
                new CompoundStatement(
                        new AssignmentStatement(new ConstantExpression(new StringValue("egreg")), "v"),
                        new PrintStatement(new VariableExpression("v"))
                )
        );
    }

    private static Statement ex2() {
        return new CompoundStatement(
                new VariableDeclarationStatement(new IntType(), "a"),
                new CompoundStatement(
                        new VariableDeclarationStatement(new IntType(), "b"),
                        new CompoundStatement(
                                new AssignmentStatement(
                                        new BinaryOperatorExpression("+",
                                                new ConstantExpression(new IntValue(2)),
                                                new BinaryOperatorExpression("*",
                                                        new ConstantExpression(new IntValue(3)),
                                                        new ConstantExpression(new IntValue(5))
                                                )
                                        ),
                                        "a"
                                ),
                                new CompoundStatement(
                                        new AssignmentStatement(
                                                new BinaryOperatorExpression("+",
                                                        new VariableExpression("a"),
                                                        new ConstantExpression(new IntValue(1))
                                                ),
                                                "b"
                                        ),
                                        new PrintStatement(new VariableExpression("b"))
                                )
                        )
                )
        );
    }

    private static Statement ex3() {
        return new CompoundStatement(
                new VariableDeclarationStatement(new BoolType(), "a"),
                new CompoundStatement(
                        new VariableDeclarationStatement(new IntType(), "v"),
                        new CompoundStatement(
                                new AssignmentStatement(new ConstantExpression(new BooleanValue(true)), "a"),
                                new CompoundStatement(
                                        new IfStatement(
                                                new VariableExpression("a"),
                                                new AssignmentStatement(new ConstantExpression(new IntValue(2)), "v"),
                                                new AssignmentStatement(new ConstantExpression(new IntValue(3)), "v")
                                        ),
                                        new PrintStatement(new VariableExpression("v"))
                                )
                        )
                )
        );
    }

    private static Statement fileExample() {
        return new CompoundStatement(
                new VariableDeclarationStatement(new StringType(), "varf"),
                new CompoundStatement(
                        new AssignmentStatement(new ConstantExpression(new StringValue("test.in")), "varf"),
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

    private static Statement ex4() {
        return new CompoundStatement(
                new VariableDeclarationStatement(new RefType(new IntType()), "v"),
                new CompoundStatement(
                        new NewStatement("v", new ConstantExpression(new IntValue(20))),
                        new CompoundStatement(
                                new PrintStatement(new ReadHeapExpression(new VariableExpression("v"))),
                                new CompoundStatement(
                                        new WriteHeapStatement("v", new ConstantExpression(new IntValue(30))),
                                        new PrintStatement(
                                                new BinaryOperatorExpression("+",
                                                        new ReadHeapExpression(new VariableExpression("v")),
                                                        new ConstantExpression(new IntValue(5))
                                                )
                                        )
                                )
                        )
                )
        );
    }

    private static Statement gcExample() {
        return new CompoundStatement(
                new VariableDeclarationStatement(new RefType(new IntType()), "v"),
                new CompoundStatement(
                        new NewStatement("v", new ConstantExpression(new IntValue(20))),
                        new CompoundStatement(
                                new VariableDeclarationStatement(new RefType(new IntType()), "a"),
                                new CompoundStatement(
                                        new NewStatement("a", new ConstantExpression(new IntValue(30))),
                                        new AssignmentStatement(new ConstantExpression(new IntValue(0)), "v")
                                )
                        )
                )
        );
    }

    private static Statement whileExample() {
        Expression cond = new RelationalExpression(">", new VariableExpression("v"), new ConstantExpression(new IntValue(0))
        );

        Statement body = new CompoundStatement(
                new PrintStatement(new VariableExpression("v")),
                new AssignmentStatement(
                        new BinaryOperatorExpression("-", new VariableExpression("v"), new ConstantExpression(new IntValue(1))),
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

    private static Statement forkExample() {
        return new CompoundStatement(
                new VariableDeclarationStatement(new IntType(), "v"),
                new CompoundStatement(
                        new VariableDeclarationStatement(new RefType(new IntType()), "a"),
                        new CompoundStatement(
                                new AssignmentStatement(new ConstantExpression(new IntValue(10)), "v"),
                                new CompoundStatement(
                                        new NewStatement("a", new ConstantExpression(new IntValue(22))),
                                        new CompoundStatement(
                                                new ForkStatement(
                                                        new CompoundStatement(
                                                                new WriteHeapStatement("a", new ConstantExpression(new IntValue(30))),
                                                                new CompoundStatement(
                                                                        new AssignmentStatement(new ConstantExpression(new IntValue(32)), "v"),
                                                                        new CompoundStatement(
                                                                                new PrintStatement(new VariableExpression("v")),
                                                                                new PrintStatement(new ReadHeapExpression(new VariableExpression("a")))
                                                                        )
                                                                )
                                                        )
                                                ),
                                                new CompoundStatement(
                                                        new PrintStatement(new VariableExpression("v")),
                                                        new PrintStatement(new ReadHeapExpression(new VariableExpression("a")))
                                                )
                                        )
                                )
                        )
                )
        );
    }

    private static Statement forExample1() {
        // for(v=0; v<3; v=v+1) print(v)
        return new ForStatement(
                "v",
                new ConstantExpression(new IntValue(0)),
                new ConstantExpression(new IntValue(3)),
                new BinaryOperatorExpression("+", new VariableExpression("v"), new ConstantExpression(new IntValue(1))),
                new PrintStatement(new VariableExpression("v"))
        );
    }

    private static Statement forExample(){
        Statement initA = new VariableDeclarationStatement(new RefType(new IntType()), "a");
        Statement allocA = new NewStatement("a", new ConstantExpression(new IntValue(20)));

        Statement body = new  CompoundStatement(
                new PrintStatement(new VariableExpression("v")),
                new AssignmentStatement(
                        new BinaryOperatorExpression("*",
                                new VariableExpression("v"),
                                new ReadHeapExpression(new VariableExpression("a"))
                        ),
                        "v"
                )
        );

        Statement forStatement = new ForStatement(
                "v",
                new ConstantExpression(new IntValue(0)),
                new ConstantExpression(new IntValue(3)),
                new BinaryOperatorExpression("+",
                        new VariableExpression("v"),
                        new ConstantExpression(new IntValue(1))),
                new ForkStatement(body)

        );
        return new CompoundStatement(
                initA,
                new CompoundStatement(
                        allocA,
                        new CompoundStatement(
                                forStatement,
                                new PrintStatement(new ReadHeapExpression(new VariableExpression("a")))
                        )
                )
        );
    }

    private static Statement condAssExample() {
        Statement decl = new CompoundStatement(
                new VariableDeclarationStatement(new RefType(new IntType()), "a"),
                new CompoundStatement(
                        new VariableDeclarationStatement(new RefType(new IntType()), "b"),
                        new VariableDeclarationStatement(new IntType(), "v")
                )
        );

        Statement init = new CompoundStatement(
                new NewStatement("a", new ConstantExpression(new IntValue(0))),
                new CompoundStatement(
                        new NewStatement("b", new ConstantExpression(new IntValue(0))),
                        new CompoundStatement(
                                new WriteHeapStatement("a", new ConstantExpression(new IntValue(1))),
                                new WriteHeapStatement("b", new ConstantExpression(new IntValue(2)))
                        )
                )
        );

        Statement condA = new ConditionalAssignmentStatement(
                "v",
                new RelationalExpression("<",
                        new ReadHeapExpression(new VariableExpression("a")),
                        new  ReadHeapExpression(new VariableExpression("b"))
                ),
                new ConstantExpression(new IntValue(100)),
                new ConstantExpression(new  IntValue(200))
        );
        Statement condB = new ConditionalAssignmentStatement(
                "v",
                new RelationalExpression(">",
                        new BinaryOperatorExpression("-",
                                new ReadHeapExpression(new VariableExpression("b")),
                                new ConstantExpression(new IntValue(2))),
                        new  ReadHeapExpression(new VariableExpression("a"))),
                new ConstantExpression(new IntValue(100)),
                new ConstantExpression(new  IntValue(200))
        );

        return new CompoundStatement(
                decl,
                new CompoundStatement(
                        init,
                        new CompoundStatement(
                                condA,
                                new CompoundStatement(
                                        new PrintStatement(new VariableExpression("v")),
                                        new CompoundStatement(
                                                condB,
                                                new PrintStatement(new VariableExpression("v"))
                                        )
                                )
                        )
                )
        );
    }

    private static Statement sleepExample(){
        Statement forkBody = new  CompoundStatement(
                new AssignmentStatement(new BinaryOperatorExpression("-",
                        new VariableExpression("v"), new ConstantExpression(new IntValue(1))),
                        "v"),
                new CompoundStatement(
                        new AssignmentStatement(new BinaryOperatorExpression("-",
                                new VariableExpression("v"), new ConstantExpression(new IntValue(1))),
                                "v"),
                        new PrintStatement(new VariableExpression("v"))
                )
        );

        return new CompoundStatement(
                new VariableDeclarationStatement(new IntType(), "v"),
                new CompoundStatement(
                        new AssignmentStatement(new ConstantExpression(new IntValue(10)), "v" ),
                        new CompoundStatement(
                                new ForkStatement(forkBody),
                                new CompoundStatement(
                                        new SleepStatement(10),
                                        new PrintStatement(new BinaryOperatorExpression("*",
                                                new ConstantExpression(new IntValue(10)), new VariableExpression("v")))
                                )
                        )
                )
        );
    }

    private static Statement getWaitExample() {
        return new CompoundStatement(
                new VariableDeclarationStatement(new IntType(), "v"),
                new CompoundStatement(
                        new AssignmentStatement(new ConstantExpression(new IntValue(20)), "v"),
                        new CompoundStatement(
                                new PrintStatement(new VariableExpression("v")),
                                new CompoundStatement(
                                        new WaitStatement(10),
                                        new PrintStatement(
                                                new BinaryOperatorExpression(
                                                        "*",
                                                        new VariableExpression("v"),
                                                        new ConstantExpression(new IntValue(10))
                                                )
                                        )
                                )
                        )
                )
        );
    }

    private static Statement switchExample() {
        Statement decls = new CompoundStatement(
                new VariableDeclarationStatement(new IntType(), "a"),
                new CompoundStatement(
                        new VariableDeclarationStatement(new IntType(), "b"),
                        new VariableDeclarationStatement(new IntType(), "c")
                )
        );

        Statement inits = new CompoundStatement(
                new AssignmentStatement(new ConstantExpression(new IntValue(1)), "a"),
                new CompoundStatement(
                        new AssignmentStatement(new ConstantExpression(new IntValue(2)), "b"),
                        new AssignmentStatement(new ConstantExpression(new IntValue(5)), "c")
                )
        );

        Expression switchExp =
                new BinaryOperatorExpression("*",
                        new VariableExpression("a"),
                        new ConstantExpression(new IntValue(10))
                );

        Expression case1Exp =
                new BinaryOperatorExpression("*",
                        new VariableExpression("b"),
                        new VariableExpression("c")
                );

        Statement case1Stmt =
                new CompoundStatement(
                        new PrintStatement(new VariableExpression("a")),
                        new PrintStatement(new VariableExpression("b"))
                );

        Expression case2Exp = new ConstantExpression(new IntValue(10));

        Statement case2Stmt =
                new CompoundStatement(
                        new PrintStatement(new ConstantExpression(new IntValue(100))),
                        new PrintStatement(new ConstantExpression(new IntValue(200)))
                );

        Statement defaultStmt =
                new PrintStatement(new ConstantExpression(new IntValue(300)));

        Statement sw =
                new SwitchStatement(
                        switchExp,
                        case1Exp, case1Stmt,
                        case2Exp, case2Stmt,
                        defaultStmt
                );

        return new CompoundStatement(
                decls,
                new CompoundStatement(
                        inits,
                        new CompoundStatement(
                                sw,
                                new PrintStatement(new ConstantExpression(new IntValue(300)))
                        )
                )
        );
    }

    private static Statement repeatUntilForkExample() {
        // fork(print(v); v=v-1)
        Statement forkBody = new CompoundStatement(
                new PrintStatement(new VariableExpression("v")),
                new AssignmentStatement(
                        new BinaryOperatorExpression("-", new VariableExpression("v"), new ConstantExpression(new IntValue(1))),
                        "v"
                )
        );

        Statement repeatBody = new CompoundStatement(
                new ForkStatement(forkBody),
                new AssignmentStatement(
                        new BinaryOperatorExpression("+", new VariableExpression("v"), new ConstantExpression(new IntValue(1))),
                        "v"
                )
        );

        Expression untilCond = new RelationalExpression(
                "==",
                new VariableExpression("v"),
                new ConstantExpression(new IntValue(3))
        );

        Statement program = new CompoundStatement(
                new VariableDeclarationStatement(new IntType(), "v"),
                new CompoundStatement(
                        new VariableDeclarationStatement(new IntType(), "x"),
                        new CompoundStatement(
                                new VariableDeclarationStatement(new IntType(), "y"),
                                new CompoundStatement(
                                        new AssignmentStatement(new ConstantExpression(new IntValue(0)), "v"),
                                        new CompoundStatement(
                                                new RepeatUntilStatement(repeatBody, untilCond),
                                                new CompoundStatement(
                                                        new AssignmentStatement(new ConstantExpression(new IntValue(1)), "x"),
                                                        new CompoundStatement(
                                                                new NoOperationStatement(),
                                                                new CompoundStatement(
                                                                        new AssignmentStatement(new ConstantExpression(new IntValue(3)), "y"),
                                                                        new CompoundStatement(
                                                                                new NoOperationStatement(),
                                                                                new PrintStatement(
                                                                                        new BinaryOperatorExpression(
                                                                                                "*",
                                                                                                new VariableExpression("v"),
                                                                                                new ConstantExpression(new IntValue(10))
                                                                                        )
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );

        return program;
    }

    public static List<ExampleProgram> buildAll() {
        List<ExampleProgram> result = new ArrayList<>();

        // Keep displayFlag=false to avoid console spam from the GUI.
        result.add(build("1", ex1(), "log1.txt"));
        result.add(build("2", ex2(), "log2.txt"));
        result.add(build("3", ex3(), "log3.txt"));
        result.add(build("4", ex4(), "log4.txt"));
        result.add(build("5", gcExample(), "log5.txt"));
        result.add(build("6", whileExample(), "log6.txt"));
        result.add(build("7", fileExample(), "logFile.txt"));
        result.add(build("8", forkExample(), "log7.txt"));
        result.add(build("9", forExample(), "log9.txt"));
        result.add(build("10", condAssExample(), "log10.txt"));
        result.add(build("11", sleepExample(), "log11.txt"));
        result.add(build("12", getWaitExample(), "logWait.txt"));
        result.add(build("13", switchExample(), "logSwitch.txt"));
        result.add(build("14", repeatUntilForkExample(), "logRepeat.txt"));

        return result;
    }

    private static ExampleProgram buildWithoutCheck(String name, Statement stmt, String logFile) {
        ProgramState prg = makeProgram(stmt);
        Repository repo = new ArrayRepository(prg, logFile);
        Controller ctrl = new Controller(repo, false);
        return new ExampleProgram(name, stmt, ctrl);
    }

    private static ExampleProgram build(String name, Statement stmt, String logFile) {
        try {
            // Standard programs call the typechecker
            stmt.typeCheck(new MapTypeEnv<>());
            ProgramState prg = makeProgram(stmt);
            Repository repo = new ArrayRepository(prg, logFile);
            Controller ctrl = new Controller(repo, false);
            return new ExampleProgram(name, stmt, ctrl);
        } catch (Exception e) {
            System.out.println("Typecheck failed for " + name + ": " + e.getMessage());
            return null;
        }
    }
}
