package controller;

import exceptions.EmptyStackException;
import model.state.ExecutionStack;
import model.state.ProgramState;
import model.statement.Statement;
import model.value.RefValue;
import model.value.Value;
import repository.Repository;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import java.util.*;
import java.util.stream.Collectors;

public class Controller {
    private final Repository repository;
    private final boolean displayFlag;
    private ExecutorService executor;


    public Controller(Repository repository, boolean displayFlag) {
        this.repository = repository;
        this.displayFlag = displayFlag;
    }

    public Map<Integer, Value> unsafeGarbageCollector(List<Integer> symTableAddress, Map<Integer, Value> heap) {
        return heap.entrySet().stream()
                .filter(e -> symTableAddress.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Map<Integer, Value> safeGarbageCollector(Collection<Integer> rootAddresses,
                                             Map<Integer, Value> heap) {

        Set<Integer> visited = new HashSet<>();
        Stack<Integer> stack = new Stack<>();

        // add initial roots
        for (Integer addr : rootAddresses) {
            if (addr != 0) {
                stack.push(addr);
            }
        }

        // follow all heap references recursively
        while (!stack.isEmpty()) {
            int address = stack.pop();

            if (!visited.contains(address) && heap.containsKey(address)) {
                visited.add(address);

                Value value = heap.get(address);
                if (value instanceof RefValue ref) {
                    int innerAddress = ref.getAddress();
                    if (innerAddress != 0) {
                        stack.push(innerAddress);
                    }
                }
            }
        }

        // keep only reachable addresses
        return heap.entrySet().stream()
                .filter(e -> visited.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    //scans the Symbol Tables of all currently running threads and extracts
    // every memory address being held by RefValue variables
    private Collection<Integer> getAllRoots(List<ProgramState> prgList) {
        return prgList.stream()
                .flatMap(p -> p.getSymbolTable().getSymbolTable().values().stream())
                .filter(v -> v instanceof RefValue)
                .map(v -> ((RefValue) v).getAddress())
                .collect(Collectors.toSet());
    }

    // filter out threads that have finished executing their code
    public List<ProgramState> removeCompletedPrg(List<ProgramState> inPrgList) {
        return inPrgList.stream()
                .filter(ProgramState::isNotCompleted)
                .collect(Collectors.toList());
    }

    public void oneStepForAllPrg(List<ProgramState> prgList) throws Exception {
        // log crt state of threads
        prgList.forEach(prg -> {
            try { repository.logProgramStateExec(prg); }
            catch (Exception e) { throw new RuntimeException(e); }
        });

        List<Callable<ProgramState>> callList = prgList.stream()
                .map((ProgramState p) -> (Callable<ProgramState>) p::oneStep)
                .collect(Collectors.toList());

        //run threads simultaneously
        List<ProgramState> newPrgList = executor.invokeAll(callList).stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (Exception e) {
                        // propagate execution exceptions
                        throw new RuntimeException(e);
                    }
                })
                .filter(Objects::nonNull) // fork returns non-null children
                .collect(Collectors.toList());

        //add the possible children created by fork
        prgList.addAll(newPrgList);

        // log again
        prgList.forEach(prg -> {
            try { repository.logProgramStateExec(prg); }
            catch (Exception e) { throw new RuntimeException(e); }
        });

        repository.setPrgList(prgList);
    }

    public void allStep() throws Exception {

        executor = Executors.newFixedThreadPool(2);

        List<ProgramState> prgList = removeCompletedPrg(repository.getPrgList());

        while (!prgList.isEmpty()) {
            // Conservative GC for shared heap:
            var roots = getAllRoots(prgList);
            if (!prgList.isEmpty()) {
                // shared heap is same object; take from any prg
                var heap = prgList.get(0).getHeap();
                heap.setContent(safeGarbageCollector(roots, heap.getContent()));
            }

            oneStepForAllPrg(prgList);
            prgList = removeCompletedPrg(repository.getPrgList());
        }

        executor.shutdownNow();
        repository.setPrgList(prgList);
    }

    //for gui
    public void oneStepAllPrg() throws Exception {
        executor = Executors.newFixedThreadPool(2);

        List<ProgramState> prgList = removeCompletedPrg(repository.getPrgList());
        if (prgList.isEmpty()) {
            repository.setPrgList(prgList);
            executor.shutdownNow();
            return;
        }

        // Conservative GC for the shared heap.
        var roots = getAllRoots(prgList);
        var heap = prgList.get(0).getHeap();
        heap.setContent(safeGarbageCollector(roots, heap.getContent()));

        oneStepForAllPrg(prgList);

        prgList = removeCompletedPrg(repository.getPrgList());
        repository.setPrgList(prgList);

        executor.shutdownNow();
    }

    public List<ProgramState> getPrgList() {
        return repository.getPrgList();
    }

    private void displayProgramState(ProgramState state) {
        if(displayFlag){
            System.out.println("==== Current Program State ====");
            System.out.println(state);
            System.out.println("===============================");
        }
    }

}
