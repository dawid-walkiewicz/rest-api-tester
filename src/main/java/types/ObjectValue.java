package types;

import java.util.Map;

public class ObjectValue implements Value{
    private final Map<String, Value> values;
    public ObjectValue(Map<String, Value> values) {
        this.values = values;
    }

    public Map<String, Value> getValues() {
        return values;
    }

    public Value get(String key) {
        return values.get(key);
    }

    public Value getValue(String key) {
        return values.get(key);
    }

    @Override
    public String toString() {
        return values.toString();
    }
}
