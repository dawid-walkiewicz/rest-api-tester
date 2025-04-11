package types;

import java.util.Map;

public record ObjectValue(Map<String, Value> values) implements Value {

    public Value get(String key) {
        return values.get(key);
    }

    @Override
    public String toString() {
        return values.toString();
    }

    @Override
    public boolean applyOperator(Operator operator, Value other) throws RuntimeException {
        if(!(other instanceof ObjectValue(Map<String, Value> values1))) {
            throw new RuntimeException("Type mismatch: cannot apply operator to " + other.type());
        }

        switch (operator) {
            case EQ -> {
                if (values.size() != values1.size()) return false;
                for (String key : values.keySet()) {
                    if (!values.get(key).applyOperator(Operator.EQ, values1.get(key))) {
                        return false;
                    }
                }
                return true;
            }
            case NEQ -> {
                return !this.applyOperator(Operator.EQ, other);
            }
            default -> throw new RuntimeException("Invalid operator for object: " + operator);
        }
    }
}
