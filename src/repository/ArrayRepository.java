package repository;

import exceptions.WritingToFile;
import model.state.ProgramState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ArrayRepository implements Repository {

    private final List<ProgramState> programs;
    private final String logFilePath;

    public ArrayRepository(ProgramState program,  String logFilePath) {
        this.programs = new ArrayList<>();
        this.programs.add(program);
        this.logFilePath = logFilePath;
    }

    @Override
    public void setPrgList(List<ProgramState> prgList) {
        programs.clear();
        programs.addAll(prgList);
    }

    @Override
    public void addProgram(ProgramState program) {
        programs.add(program);
    }

    @Override
    public List<ProgramState> getPrgList() {
        return programs;
    }

    @Override
    public void logProgramStateExec(ProgramState programState) throws WritingToFile{
        try (PrintWriter logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)))) {
            logFile.println(programState.toString());
            logFile.println("--------------------------------------------------");
        } catch (IOException e) {
            throw new WritingToFile("Error writing to log file: " + e.getMessage());
        }
    }
}
