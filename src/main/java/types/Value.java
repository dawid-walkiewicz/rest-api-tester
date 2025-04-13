package types;

public interface Value {
    default String type() {
        return this.getClass().getSimpleName();
    }
    boolean applyOperator(Operator operator, Value other) throws RuntimeException;
}
