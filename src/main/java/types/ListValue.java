package types;

import java.util.List;

public record ListValue(List<Value> values) implements Value {

    public Value get(int index) {
        return values.get(index);
    }

    @Override
    public String type() {
        return "List";
    }

    @Override
    public boolean applyOperator(Operator operator, Value other) throws RuntimeException {
        if (!(other instanceof ListValue(List<Value> values1))) {
            throw new RuntimeException("Type mismatch: cannot apply operator to " + other.type());
        }
        switch (operator) {
            case EQ -> {
                if (values.size() != values1.size()) return false;
                for (int i = 0; i < values.size(); i++) {
                    if (!values.get(i).applyOperator(Operator.EQ, values1.get(i))) {
                        return false;
                    }
                }
                return true;
            }
            case NEQ -> {
                return !applyOperator(Operator.EQ, other);
            }
            default -> throw new RuntimeException("Invalid operator for list: " + operator);
        }
    }

    @Override
    public String toString() {
        return values.toString();
    }
}
