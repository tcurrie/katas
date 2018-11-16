package katas.twentyfour;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import katas.Combinations;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
@RunWith(Parameterized.class)
public class AllPermutationsTest {

    @Parameterized.Parameter(0)
    public List<Integer> values;

    @Parameterized.Parameter(1)
    public Integer choose;

    @Parameterized.Parameter(2)
    public List<List<Integer>> expected;

    @Parameterized.Parameters(name="{index}: values({0}), choose {1}, expect ({2})")
    public static Object[][] data() {
        return new Object[][] {
            {list(), 0, list()},
            {list(1), 0, list()},
            {list(), 1, list()},
            {list(1), 1, list(list(1))},
            {list(1, 2), 1, list(list(1), list(2))},
            {list(1, 2, 3), 1, list(list(1), list(2), list(3))},
            {list(1, 2), 2, list(
                list(1, 1), list(1, 2),
                list(2, 1), list(2, 2))},
            {list(1, 2, 3), 2, list(
                list(1, 1), list(1, 2), list(1, 3),
                list(2, 1), list(2, 2), list(2, 3),
                list(3, 1), list(3, 2), list(3, 3))},
            {list(1, 2, 3), 3, list(
                list(1, 1, 1), list(1, 1, 2), list(1, 1, 3),
                list(1, 2, 1), list(1, 2, 2), list(1, 2, 3),
                list(1, 3, 1), list(1, 3, 2), list(1, 3, 3),
                list(2, 1, 1), list(2, 1, 2), list(2, 1, 3),
                list(2, 2, 1), list(2, 2, 2), list(2, 2, 3),
                list(2, 3, 1), list(2, 3, 2), list(2, 3, 3),
                list(3, 1, 1), list(3, 1, 2), list(3, 1, 3),
                list(3, 2, 1), list(3, 2, 2), list(3, 2, 3),
                list(3, 3, 1), list(3, 3, 2), list(3, 3, 3)
            )},
        };
    }

    @Test
    public void validate() {
        assertThat(Combinations.getAllPermutations(values, choose).collect(Collectors.toSet()), is(new HashSet<>(expected)));
    }

    @SafeVarargs
    private static <T> List<T> list(T... v) {
        return Arrays.asList(v);
    }
}
