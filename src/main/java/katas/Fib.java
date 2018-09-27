package katas;

import java.math.BigInteger;
import java.util.Map;
import java.util.function.Function;

import tcurrie.ycombinator.Y;

public final class Fib {
    private Fib() {}

    public static final Function<Integer, BigInteger> ITERATIVE = index -> {
        if (index == 0) return BigInteger.ZERO;
        BigInteger a = BigInteger.ZERO;
        BigInteger b = BigInteger.ONE;
        for (int n = 1; n < index; n++) {
            final BigInteger c = a.add(b);
            a = b;
            b = c;
        }
        return b;
    };

    public static final Function<Integer, BigInteger> RECURSIVE = Y.Combinator.of(y -> index -> {
        if (index == 0) return BigInteger.ZERO;
        if (index == 1) return BigInteger.ONE;
        return y.apply(index - 2).add(y.apply(index - 1));
    });

    private static final Map<Integer, BigInteger> KNOWN = new TepidMap<>(Maps.adapt(Maps.toIndexMap(0l, 1l, 1l, 2l), k->k, BigInteger::valueOf));
    private static final BigInteger TWO = BigInteger.valueOf(2);
    public static final Function<Integer, BigInteger> FAST_DOUBLING_WITH_WEAK_STORE = Y.Combinator.of(y -> index -> {
        if (KNOWN.containsKey(index)) {
            return KNOWN.get(index);
        }
        final int half = index / 2;
        final BigInteger previous = y.apply(half);
        final BigInteger next = y.apply(half + 1);

        final BigInteger result;

        if ((index & 1) == 1) {
            result = previous.multiply(previous).add(next.multiply(next));
        } else {
            result = previous.multiply(TWO.multiply(next).subtract(previous));
        }

        KNOWN.put(index, result);
        return result;
    });
}
