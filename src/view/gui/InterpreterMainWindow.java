package view.gui;

import controller.Controller;
import model.state.ProgramState;
import model.statement.Statement;
import model.value.Value;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class InterpreterMainWindow {

    private final Controller controller;
    private final Stage stage;

    // (a) number of PrgStates
    private final TextField prgCountField = new TextField();

    // (b) HeapTable: address, value
    private final TableView<HeapEntry> heapTable = new TableView<>();
    private final ObservableList<HeapEntry> heapItems = FXCollections.observableArrayList();

    // (c) Out list
    private final ListView<String> outList = new ListView<>();

    // (d) FileTable list
    private final ListView<String> fileTableList = new ListView<>();

    // (e) list of PrgState identifiers
    private final ListView<Integer> prgIdList = new ListView<>();

    // (f) SymTable: var, value for selected PrgState
    private final TableView<SymTableEntry> symTable = new TableView<>();
    private final ObservableList<SymTableEntry> symItems = FXCollections.observableArrayList();

    // (g) ExeStack for selected PrgState
    private final ListView<String> exeStackList = new ListView<>();

    // (h) Run one step button
    private final Button runOneStepButton = new Button("Run one step");

    private ProgramState lastDisplayedState = null;


    public InterpreterMainWindow(Controller controller) {
        this.controller = Objects.requireNonNull(controller);
        this.stage = new Stage();

        stage.setTitle("Toy Language Interpreter");
        stage.setScene(buildScene());

        // default UI init
        prgCountField.setEditable(false);
        configureTables();
        wireEvents();
        refreshAll();
    }

    public void show() {
        stage.show();
    }

    private Scene buildScene() {
        // Left: ids + exe stack
        VBox left = new VBox(8,
                labeled("Program State IDs", prgIdList),
                labeled("Execution Stack", exeStackList)
        );
        left.setPadding(new Insets(10));
        VBox.setVgrow(prgIdList, Priority.ALWAYS);
        VBox.setVgrow(exeStackList, Priority.ALWAYS);

        // Center: heap + symtable
        VBox center = new VBox(8,
                labeled("Heap", heapTable),
                labeled("Symbol Table", symTable)
        );
        center.setPadding(new Insets(10));
        VBox.setVgrow(heapTable, Priority.ALWAYS);
        VBox.setVgrow(symTable, Priority.ALWAYS);

        // Right: out + file table
        VBox right = new VBox(8,
                labeled("Out", outList),
                labeled("File Table", fileTableList)
        );
        right.setPadding(new Insets(10));
        VBox.setVgrow(outList, Priority.ALWAYS);
        VBox.setVgrow(fileTableList, Priority.ALWAYS);

        // Top: program count
        HBox top = new HBox(10,
                new Label("Number of Program States:"),
                prgCountField
        );
        top.setPadding(new Insets(10));

        // Bottom: run one step
        HBox bottom = new HBox(10, runOneStepButton);
        bottom.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setTop(top);
        root.setLeft(left);
        root.setCenter(center);
        root.setRight(right);
        root.setBottom(bottom);

        // reasonable defaults
        root.setPrefSize(1100, 650);
        return new Scene(root);
    }

    private static <T extends Control> VBox labeled(String title, T control) {
        Label label = new Label(title);
        VBox box = new VBox(4, label, control);
        VBox.setVgrow(control, Priority.ALWAYS);
        return box;
    }

    private void configureTables() {
        // Heap table
        TableColumn<HeapEntry, String> addrCol = new TableColumn<>("Address");
        addrCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        addrCol.setPrefWidth(120);

        TableColumn<HeapEntry, String> valCol = new TableColumn<>("Value");
        valCol.setCellValueFactory(new PropertyValueFactory<>("value"));
        valCol.setPrefWidth(350);

        heapTable.getColumns().setAll(addrCol, valCol);
        heapTable.setItems(heapItems);

        // SymTable
        TableColumn<SymTableEntry, String> nameCol = new TableColumn<>("Variable");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(160);

        TableColumn<SymTableEntry, String> symValCol = new TableColumn<>("Value");
        symValCol.setCellValueFactory(new PropertyValueFactory<>("value"));
        symValCol.setPrefWidth(300);

        symTable.getColumns().setAll(nameCol, symValCol);
        symTable.setItems(symItems);
    }

    private void wireEvents() {
        prgIdList.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> refreshSelectedPrgState());

        runOneStepButton.setOnAction(e -> {
            try {
                controller.oneStepAllPrg();
                refreshAll();
            } catch (Exception ex) {
                showError(ex);
            }
        });
    }

    private void refreshAll() {
        List<ProgramState> prgList = controller.getPrgList();
        prgCountField.setText(String.valueOf(prgList.size()));
        runOneStepButton.setDisable(prgList.isEmpty());

        // IDs list
        List<Integer> ids = prgList.stream()
                .map(ProgramState::getId)
                .sorted()
                .toList();
        prgIdList.setItems(FXCollections.observableArrayList(ids));

        // Keep selection stable if possible
        if (!ids.isEmpty()) {
            Integer currentlySelected = prgIdList.getSelectionModel().getSelectedItem();
            if (currentlySelected != null && ids.contains(currentlySelected)) {
                prgIdList.getSelectionModel().select(currentlySelected);
            } else {
                prgIdList.getSelectionModel().selectFirst();
            }
        }

        // Heap / Out / FileTable are shared (heap/out/filetable are conceptually shared in this project setup)
        if (!prgList.isEmpty()) {
            ProgramState any = prgList.get(0);
            lastDisplayedState = any;

            refreshHeap(any);
            refreshOut(any);
            refreshFileTable(any);
        } else if (lastDisplayedState != null) {
            // program finished → keep final state visible
            refreshHeap(lastDisplayedState);
            refreshOut(lastDisplayedState);
            refreshFileTable(lastDisplayedState);
        }


        refreshSelectedPrgState();
    }

    private void refreshHeap(ProgramState prg) {
        heapItems.setAll(
                prg.getHeap().getContent().entrySet().stream()
                        .sorted(Comparator.comparingInt(Map.Entry::getKey))
                        .map(e -> new HeapEntry(String.valueOf(e.getKey()), String.valueOf(e.getValue())))
                        .toList()
        );
    }

    private void refreshOut(ProgramState prg) {
        List<String> out = prg.getOut().getOut().stream()
                .map(String::valueOf)
                .toList();
        outList.setItems(FXCollections.observableArrayList(out));
    }

    private void refreshFileTable(ProgramState prg) {
        List<String> files = prg.getFileTable().getContent().keySet().stream()
                .map(String::valueOf)
                .sorted()
                .toList();
        fileTableList.setItems(FXCollections.observableArrayList(files));
    }

    private void refreshSelectedPrgState() {
        Integer id = prgIdList.getSelectionModel().getSelectedItem();
        if (id == null) {
            symItems.clear();
            exeStackList.setItems(FXCollections.observableArrayList());
            return;
        }

        ProgramState prg = controller.getPrgList().stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(lastDisplayedState);

        if (prg == null) {
            return;
        }


        // SymTable
        symItems.setAll(
                prg.getSymbolTable().getSymbolTable().entrySet().stream()
                        .sorted(Map.Entry.comparingByKey())
                        .map(e -> new SymTableEntry(e.getKey(), String.valueOf(e.getValue())))
                        .toList()
        );

        // ExeStack: top element first, then next, etc.
        List<Statement> stack = prg.getExecutionStack().getStack();
        List<String> stackView = stack.stream().map(String::valueOf).collect(Collectors.toList());
        exeStackList.setItems(FXCollections.observableArrayList(stackView));
    }

    private void showError(Exception ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Execution error");
        alert.setHeaderText(ex.getClass().getSimpleName());
        alert.setContentText(ex.getMessage());
        alert.showAndWait();
    }
}
