package katas;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public final class Primes {
    private Primes() {
        throw new UnsupportedOperationException("Do not create.");
    }
    private static final BigInteger TWO = BigInteger.valueOf(2);
    private static final BigInteger THREE = BigInteger.valueOf(3);
    private static final BigInteger FIVE = BigInteger.valueOf(5);
    private static final BigInteger SIX = BigInteger.valueOf(6);

    public static List<BigInteger> factors(BigInteger current) {
        final ArrayList<BigInteger> factors = new ArrayList<>();
        current = reduce(current, factors, TWO);
        current = reduce(current, factors, THREE);
        for (BigInteger i = FIVE;
             i.multiply(i).compareTo(current) <= 0;
             i = i.add(SIX)) {
            current = reduce(current, factors, i);
            current = reduce(current, factors, i.add(TWO));
        }
        if (current.compareTo(BigInteger.ONE) > 0)
            factors.add(current);
        return factors;
    }

    private static BigInteger reduce(BigInteger current, final ArrayList<BigInteger> factors, final BigInteger factor) {
        BigInteger[] next = current.divideAndRemainder(factor);
        while (next[1].compareTo(BigInteger.ZERO) == 0 && current.compareTo(BigInteger.ZERO) > 0) {
            factors.add(factor);
            current = next[0];
            next = current.divideAndRemainder(factor);
        }
        return current;
    }
}
