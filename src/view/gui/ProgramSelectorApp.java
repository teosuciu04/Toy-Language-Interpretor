package view.gui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class ProgramSelectorApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        List<ExampleProgram> programs = ExampleProgramsFactory.buildAll();

        ListView<ExampleProgram> listView = new ListView<>();
        listView.setItems(FXCollections.observableArrayList(programs));

        Button selectButton = new Button("Select program");
        selectButton.setDisable(true);

        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            selectButton.setDisable(newV == null);
        });

        selectButton.setOnAction(e -> {
            ExampleProgram selected = listView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                return;
            }

            InterpreterMainWindow mainWindow = new InterpreterMainWindow(selected.getController());
            mainWindow.show();
        });

        VBox root = new VBox(10,
                new Label("Select a program to execute:"),
                listView,
                selectButton
        );
        root.setPadding(new Insets(10));
        VBox.setVgrow(listView, Priority.ALWAYS);
        root.setPrefSize(900, 450);

        primaryStage.setTitle("Program Selection");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
