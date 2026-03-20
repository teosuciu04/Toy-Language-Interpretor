package model.value;

import com.sun.jdi.BooleanType;
import model.type.BoolType;
import model.type.Type;

public record BooleanValue(boolean value) implements Value{

    public boolean getValue() {

        return value;
    }

    @Override
    public Type getType() {

        return new BoolType();
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }


}
