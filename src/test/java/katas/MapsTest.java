package katas;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class MapsTest {
    @Test
    public void shouldMapStringArrayByIndex() {
        final String[] data = { "a", "b", "c" };
        final Map<Integer, String> expected = new HashMap<>();
        expected.put(0, "a");
        expected.put(1, "b");
        expected.put(2, "c");
        assertThat(Maps.toIndexMap(data), is(expected));
    }

    @Test
    public void shouldMapIntArrayByIndex() {
        final Integer[] data = { 6, 5, 4, 3, 2 };
        final Map<Integer, Integer> expected = new HashMap<>();
        expected.put(0, 6);
        expected.put(1, 5);
        expected.put(2, 4);
        expected.put(3, 3);
        expected.put(4, 2);
        assertThat(Maps.toIndexMap(data), is(expected));
    }

    @Test
    public void shouldAdaptIntegerKeysAndValuesToStrings() {
        final Map<Integer, Integer> initial = Maps.toIndexMap(6, 5, 4, 3, 2 );
        final Map<String, String> expected = new HashMap<>();
        expected.put("0", "6");
        expected.put("1", "5");
        expected.put("2", "4");
        expected.put("3", "3");
        expected.put("4", "2");
        assertThat(Maps.adapt(initial, String::valueOf, String::valueOf), is(expected));
    }

    @Test
    public void shouldAdaptStringKeysToIntegerAndLongValueToBigInt() {
        final Map<String, Long> initial = new HashMap<>();
        initial.put("0", 6L);
        initial.put("1", 5L);
        initial.put("2", 4L);
        initial.put("3", 3L);
        initial.put("4", 2L);
        final Map<Integer, BigInteger> expected = new HashMap<>();
        expected.put(0, BigInteger.valueOf(6));
        expected.put(1, BigInteger.valueOf(5));
        expected.put(2, BigInteger.valueOf(4));
        expected.put(3, BigInteger.valueOf(3));
        expected.put(4, BigInteger.valueOf(2));
        assertThat(Maps.adapt(initial, Integer::parseInt, BigInteger::valueOf), is(expected));
    }
}
