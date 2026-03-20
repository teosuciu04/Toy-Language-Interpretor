package view.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SymTableEntry {
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty value = new SimpleStringProperty();

    public SymTableEntry(String name, String value) {
        this.name.set(name);
        this.value.set(value);
    }

    public String getName() { return name.get(); }
    public StringProperty nameProperty() { return name; }

    public String getValue() { return value.get(); }
    public StringProperty valueProperty() { return value; }
}
