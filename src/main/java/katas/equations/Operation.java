package katas.equations;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public class Operation implements Equation {
    private final Equation left;
    private final Equation right;
    private final Operator operator;

    public static Stream<BiFunction<Equation, Equation, Equation>> factoriesOf(final Operator o) {
        if (o.isCommutative()) {
            return Stream.of((l, r) -> Operation.of(l, r, o));
        } else {
            return Stream.of((l, r) -> Operation.of(l, r, o), (l, r) -> Operation.of(r, l, o));
        }
    }

    public static Operation of(final Equation left, final Equation right, final Operator operator) {
        return new Operation(left, right, operator);
    }

    private Operation(final Equation left, final Equation right, final Operator operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }


    @Override
    public double getResult() {
        return operator.apply(left.getResult(), right.getResult());
    }

    @Override
    public String toString() {
        return String.format("(%s %s %s)", left, operator.getSymbol(), right);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Operation that = (Operation) o;

        return operator == that.operator
            && (Objects.equals(left, that.left) && Objects.equals(right, that.right)
                        || operator.isCommutative() && Objects.equals(left, that.right) && Objects.equals(right, that.left));
    }

    @Override
    public int hashCode() {
        if (operator.isCommutative()) {
            long[] operands = {left.hashCode(), right.hashCode()};
            Arrays.sort(operands);
            return Objects.hash(operands[0], operands[1], operator);
        } else {
            return Objects.hash(left, right, operator);
        }
    }
}
