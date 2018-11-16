package katas.equations;

import java.util.Objects;

public class Value implements Equation {
    private final double value;

    public static Value of(final double value) {
        return new Value(value);
    }

    private Value(final double value) {
        this.value = value;
    }

    @Override
    public double getResult() {
        return value;
    }

    @Override
    public String toString() {
        return value == (int) value ? String.valueOf((int)value) : String.valueOf(value);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Value value1 = (Value) o;
        return Double.compare(value1.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
