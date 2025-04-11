package types;

public record StringValue(String value) implements Value {

    @Override
    public String type() {
        return "String";
    }

    @Override
    public boolean applyOperator(Operator operator, Value other) throws RuntimeException {
        if(!(other instanceof StringValue(String value1))) {
            throw new RuntimeException("Type mismatch: cannot apply operator to " + other.type());
        }

        return switch (operator) {
            case EQ -> value.equals(value1);
            case NEQ -> !value.equals(value1);
            case LT -> value.compareTo(value1) < 0;
            case GT -> value.compareTo(value1) > 0;
            case LTE -> value.compareTo(value1) <= 0;
            case GTE -> value.compareTo(value1) >= 0;
        };
    }

    @Override
    public String toString() {
        return value;
    }
}
