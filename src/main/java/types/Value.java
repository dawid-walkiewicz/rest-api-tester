package types;

public interface Value {
    default String type() {
        return this.getClass().getSimpleName();
    }
}
