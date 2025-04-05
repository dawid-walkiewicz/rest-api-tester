package types;

public class StringValue implements Value{
    private final String value;

    public StringValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String type() {
        return "String";
    }

    @Override
    public String toString() {
        return value;
    }
}
