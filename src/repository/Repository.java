package repository;

import model.state.ProgramState;
import java.util.List;

public interface Repository {
    void addProgram(ProgramState program);

    List<ProgramState> getPrgList();
    void setPrgList(List<ProgramState> prgList);

    void logProgramStateExec(ProgramState programState) throws Exception;
}
