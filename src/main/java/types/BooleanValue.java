package types;

public class BooleanValue implements Value{
    private final boolean value;

    public BooleanValue(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public String type() {
        return "Boolean";
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
