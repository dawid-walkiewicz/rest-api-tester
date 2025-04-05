package types;

public class Pair implements Value {
    private final String key;
    private final Value value;

    public Pair(String first, Value second) {
        this.key = first;
        this.value = second;
    }

    public String getKey() {
        return key;
    }

    public Value getValue() {
        return value;
    }

    @Override
    public String type() {
        return "Pair";
    }

    @Override
    public String toString() {
        return "Pair{" +
                "key='" + key + '\'' +
                ", value=" + value +
                '}';
    }
}
