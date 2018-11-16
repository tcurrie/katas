package katas.equations;

import java.util.function.BiFunction;

public enum Operator {
    ADDITION("+", true, (a, b)->a+b),
    SUBTRACTION("-", false, (a, b)->a-b),
    MULTIPLICATION("*", true, (a, b)->a*b),
    DIVISION("/", false, (a, b)->a/b),
    POWER("^", false, Math::pow),
    MODULUS("%", false, (a, b)->a%b)
    ;

    private final String symbol;
    private final boolean commutative;
    private final BiFunction<Double, Double, Double> eq;
    private Operator(String symbol, boolean commutative, BiFunction<Double, Double, Double> eq) {
        this.symbol = symbol;
        this.commutative = commutative;
        this.eq = eq;
    }

    public String getSymbol() {
        return symbol;
    }

    public boolean isCommutative() {
        return commutative;
    }

    public double apply(final double left, final double right) {
        return eq.apply(left, right);
    }

}
