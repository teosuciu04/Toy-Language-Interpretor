package model.value;

import model.type.RefType;
import model.type.Type;

public record RefValue(int address, Type locationType) implements Value {
    public Type getType() {
        return new RefType(locationType);
    }

    @Override
    public boolean equals(Object another) {
        if (another instanceof RefValue) {
            RefValue other = (RefValue) another;
            return address == other.address &&
                    locationType.equals(other.locationType);
        }
        return false;
    }


    public String toString() {
        return "(" + address + ", " + locationType + ")";
    }

    public int getAddress() {
        return address;
    }
}
