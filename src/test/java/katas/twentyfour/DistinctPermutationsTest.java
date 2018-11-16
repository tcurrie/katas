package katas.twentyfour;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import katas.Combinations;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
@RunWith(Parameterized.class)
public class DistinctPermutationsTest {

    @Parameterized.Parameter(0)
    public Set<Integer> values;

    @Parameterized.Parameter(1)
    public Integer choose;

    @Parameterized.Parameter(2)
    public Set<List<Integer>> expected;

    @Parameterized.Parameters(name="{index}: values({0}), choose {1}, expect ({2})")
    public static Object[][] data() {
        return new Object[][] {
            {set(), 0, set()},
            {set(1), 0, set()},
            {set(), 1, set()},
            {set(1), 1, set(list(1))},
            {set(1, 2), 1, set(list(1), list(2))},
            {set(1, 2, 3), 1, set(list(1), list(2), list(3))},
            {set(1, 2), 2, set(list(1, 2), list(2, 1))},
            {set(1, 2, 3), 2, set(
                list(1, 2), list(1, 3),
                list(2, 1), list(2, 3),
                list(3, 1), list(3, 2))},
            {set(1, 2, 3), 3, set(
                list(1, 2, 3), list(1, 3, 2),
                list(2, 1, 3), list(2, 3, 1),
                list(3, 1, 2), list(3, 2, 1))},
        };
    }

    @Test
    public void validate() {
        assertThat(Combinations.getDistinctPermutations(values, choose).collect(Collectors.toSet()), is(expected));
    }

    @SafeVarargs
    private static <T> List<T> list(T... v) {
        return Arrays.asList(v);
    }

    @SafeVarargs
    private static <T> Set<T> set(T... v) {
        return new HashSet<>(Arrays.asList(v));
    }
}
