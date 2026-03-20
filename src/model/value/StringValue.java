package model.value;

import model.type.StringType;
import model.type.Type;

public class StringValue implements Value {
    private final String val;

    public StringValue(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }

    @Override
    public Type getType() {
        return new StringType();
    }

    @Override
    public boolean equals(Object another) {
        if (another instanceof StringValue other)
            return val.equals(other.val);
        return false;
    }

    @Override
    public String toString() {
        return "\"" + val + "\"";
    }
}
