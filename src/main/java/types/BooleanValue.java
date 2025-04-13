package types;

public record BooleanValue(boolean value) implements Value {

    @Override
    public String type() {
        return "Boolean";
    }

    @Override
    public boolean applyOperator(Operator operator, Value other) throws RuntimeException {
        if (!(other instanceof BooleanValue)) {
            throw new RuntimeException("Type mismatch: cannot apply operator to " + other.type());
        }

        return switch (operator) {
            case EQ -> value == ((BooleanValue) other).value;
            case NEQ -> value != ((BooleanValue) other).value;
            default -> throw new RuntimeException("Invalid operator for boolean: " + operator);
        };
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
