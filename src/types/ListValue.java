package types;

import java.util.List;

public class ListValue implements Value{
    private final List<Value> values;
    public ListValue(List<Value> values) {
        this.values = values;
    }
    public List<Value> getValues() {
        return values;
    }

    public Value get(int index) {
        return values.get(index);
    }

    @Override
    public String type() {
        return "List";
    }
}
