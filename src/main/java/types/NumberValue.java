package types;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public record NumberValue(double value) implements Value {

    @Override
    public String type() {
        return "Number";
    }

    @Override
    public boolean applyOperator(Operator operator, Value other) throws RuntimeException {
        if (!(other instanceof NumberValue(double value1))) {
            throw new RuntimeException("Type mismatch: cannot apply operator to " + other.type());
        }

        return switch (operator) {
            case EQ -> value == value1;
            case NEQ -> value != value1;
            case LT -> value < value1;
            case GT -> value > value1;
            case LTE -> value <= value1;
            case GTE -> value >= value1;
        };
    }

    @Override
    public String toString() {
        if (Double.isNaN(value)) return "NaN";

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat df = new DecimalFormat("0.###", symbols);

        return df.format(value);
    }
}
