package katas;

import java.util.function.Function;

import tcurrie.ycombinator.Y;

final class Factorial {
    private Factorial() { }

    static final Function<Integer, Integer> CALC = Y.Combinator.of(y ->
        n -> n == 0 ? 1 : n * y.apply(n - 1)
    );
}
