package view.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class HeapEntry {
    private final StringProperty address = new SimpleStringProperty();
    private final StringProperty value = new SimpleStringProperty();

    public HeapEntry(String address, String value) {
        this.address.set(address);
        this.value.set(value);
    }

    public String getAddress() { return address.get(); }
    public StringProperty addressProperty() { return address; }

    public String getValue() { return value.get(); }
    public StringProperty valueProperty() { return value; }
}
