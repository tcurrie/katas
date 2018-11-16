package katas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import tcurrie.ycombinator.Y;

public class Combinations {
    private Combinations() {
        throw new UnsupportedOperationException("Do not create.");
    }

    public static Stream<List<Double>> getDistinctCombinations(final int min, final int max, final int choices) {
        final Set<Double> values = IntStream.range(min, max).asDoubleStream().boxed().collect(Collectors.toSet());
        return getDistinctCombinations(values, choices);
    }

    public static <T> Stream<List<T>> getDistinctCombinations(final Collection<T> values) {
        return getDistinctCombinations(values, values.size());
    }

    public static <T> Stream<List<T>> getDistinctCombinations(final Collection<T> values, final int size) {
        return Y.BiCombinator.<List<T>, List<T>, Stream<List<T>>>of(
            y -> (l, r) -> IntStream.range(0, l.size()).boxed().flatMap(
                i -> {
                    final List<T> next = append(r, l.get(i));
                    if (next.size() == size) {
                        return Stream.of(next);
                    } else {
                        return y.apply(l.subList(i + 1, l.size()), next);
                    }
                }
            )
        ).apply(new ArrayList<>(values), new ArrayList<T>());
    }

    public static <T> Stream<List<T>> getDistinctPermutations(final Collection<T> values) {
        return getDistinctPermutations(values, values.size());
    }

    public static <T> Stream<List<T>> getDistinctPermutations(final Collection<T> values, final int size) {
        if (size <= 0 || size > values.size()) return Stream.empty();
        return Y.BiCombinator.<List<T>, List<T>, Stream<List<T>>>of(
            y -> (l, r) -> IntStream.range(0, l.size()).boxed().flatMap(
                i -> {
                    final List<T> next = append(r, l.get(i));
                    if (next.size() == size) {
                       return Stream.of(next);
                    } else {
                        final List<T> remaining = new ArrayList<>(l);
                        remaining.remove((int) i);
                        return y.apply(remaining, next);
                    }
                }
            )
        ).apply(new ArrayList<>(values), new ArrayList<>());
    }


    public static <T> Stream<List<T>> getAllPermutations(final Collection<T> values, final int size) {
        if (size == 0) return Stream.empty();

        return Y.Combinator.<List<T>, Stream<List<T>>>of(
            y -> r -> values.stream().flatMap(
                v -> {
                    final List<T> next = append(r, v);
                    if (next.size() == size) {
                        return Stream.of(next);
                    } else {
                        return y.apply(next);
                    }
                }
            )
        ).apply(new ArrayList<>());
    }

    private static <T> List<T> append(final List<T> r, final T add) {
        final List<T> result = new ArrayList<>(r);
        result.add(add);
        return result;
    }
}
