package katas;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TepidMapTest {
    @Rule public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldRemoveWeakKeyOnGC() {
        final TepidMap<Integer, String> map = new TepidMap<>();
        map.put(createNewInteger(1), "xyz");
        System.gc();
        await().atMost(10, TimeUnit.SECONDS).until(map::size, Matchers.is(0));
    }

    @Test
    public void shouldNotRemoveStrongKeyOnGC() {
        final TepidMap<Integer, String> map = new TepidMap<>();
        map.put(createNewInteger(0), "xyz", true);

        IntStream.range(1, 10).forEach(i->map.put(createNewInteger(i), "asdf"));
        IntStream.range(1, 10).forEach(i-> assertThat(map.get(i), is("asdf")));
        assertThat(map.size(), is(10));

        System.gc();

        await().atMost(10, TimeUnit.SECONDS).until(map::size, Matchers.is(1));
    }

    @Test
    public void shouldNotRemoveWeakJVMConstantsOnGC() {
        final TepidMap<Integer, String> map = new TepidMap<>();

        IntStream.range(0, 1000).forEach(i->map.put(i, "asdf"));
        IntStream.range(0, 1000).forEach(i-> assertThat(map.get(i), is("asdf")));
        assertThat(map.size(), is(1000));

        System.gc();

        await().atMost(10, TimeUnit.SECONDS).until(map::size, Matchers.is(128));
    }

    @Test
    public void shouldReplaceWeakKeyWithStrong() {
        final TepidMap<Integer, String> map = new TepidMap<>();
        map.put(createNewInteger(0), "xyz");
        map.put(createNewInteger(0), "pdq", true);
        assertThat(map.size(), is(1));
        assertThat(map.get(0), is("pdq"));

        IntStream.range(1, 10).forEach(i->map.put(createNewInteger(i), "asdf"));
        IntStream.range(1, 10).forEach(i-> assertThat(map.get(i), is("asdf")));
        assertThat(map.size(), is(10));

        System.gc();

        await().atMost(10, TimeUnit.SECONDS).until(map::size, Matchers.is(1));
        assertThat(map.get(0), is("pdq"));
    }

    @Test
    public void shouldReplaceStrongKeyWithWeak() {
        final TepidMap<Integer, String> map = new TepidMap<>();
        map.put(createNewInteger(1), "xyz", true);
        map.put(createNewInteger(1), "pdq");
        assertThat(map.get(1), is("pdq"));

        System.gc();
        await().atMost(10, TimeUnit.SECONDS).until(map::size, Matchers.is(0));
    }

    @Test
    public void shouldMergeStrongAndWeakKeysInKeySet() {
        final TepidMap<Integer, String> map = new TepidMap<>();
        map.put(createNewInteger(1), "xyz", true);
        map.put(createNewInteger(2), "pdq");

        final Set<Integer> expected = new HashSet<>();
        expected.add(1);
        expected.add(2);
        assertThat(map.keySet(), is(expected));
    }

    @Test
    public void shouldCollectStrongAndWeakValuesInValueCollection() {
        final TepidMap<Integer, String> map = new TepidMap<>();
        map.put(createNewInteger(1), "xyz", true);
        map.put(createNewInteger(2), "pdq");

        assertThat(map.values(), Matchers.containsInAnyOrder("xyz", "pdq"));
        assertThat(map.values().size(), is(2));
    }

    @Test
    public void shouldMergeStrongAndWeakEntriesInEntrySet() {
        final TepidMap<Integer, String> map = new TepidMap<>();
        map.put(createNewInteger(1), "xyz", true);
        map.put(createNewInteger(2), "pdq");

        final Map<Integer, String> expected = new HashMap<>();
        expected.put(createNewInteger(1), "xyz");
        expected.put(createNewInteger(2), "pdq");

        assertThat(map.entrySet(), is(expected.entrySet()));
    }

    @Test()
    public void keySetShouldNotBeModifiable() {
        thrown.expect(UnsupportedOperationException.class);
        new TepidMap<>().keySet().add(1);
    }

    @Test
    public void valueCollectionShouldNotBeModifiable() {
        thrown.expect(UnsupportedOperationException.class);
        new TepidMap<>().values().add("asdf");
    }

    @Test
    public void entrySetShouldNotBeModifiable() {
        final Map<Integer, String> expected = new HashMap<>();
        expected.put(createNewInteger(1), "xyz");

        thrown.expect(UnsupportedOperationException.class);
        new TepidMap<Integer, String>().entrySet().addAll(expected.entrySet());
    }

    @Test
    public void shouldPutAllWeakKeys() {
        final Map<Integer, String> toAdd = new WeakHashMap<>();
        toAdd.put(createNewInteger(1), "xyz");
        toAdd.put(createNewInteger(2), "pdq");
        toAdd.put(createNewInteger(3), "xyz");


        final Map<Integer, String> expectedAll = new HashMap<>();
        expectedAll.put(createNewInteger(1), "xyz");
        expectedAll.put(createNewInteger(2), "pdq");
        expectedAll.put(createNewInteger(3), "xyz");
        expectedAll.put(createNewInteger(4), "ghi");
        expectedAll.put(createNewInteger(5), "jkl");

        final Map<Integer, String> expectedStrong = new HashMap<>();
        expectedStrong.put(createNewInteger(4), "ghi");

        final TepidMap<Integer, String> map = new TepidMap<>();
        map.put(createNewInteger(1), "abc", true);
        map.put(createNewInteger(2), "def");
        map.put(createNewInteger(4), "ghi", true);
        map.put(createNewInteger(5), "jkl");
        map.putAll(toAdd);

        assertThat(map, is(expectedAll));

        System.gc();

        await().atMost(10, TimeUnit.SECONDS).until(()->map, Matchers.is(expectedStrong));
    }

    @Test
    public void shouldPutAllStrongKeys() {
        final Map<Integer, String> toAdd = new WeakHashMap<>();
        toAdd.put(createNewInteger(1), "xyz");
        toAdd.put(createNewInteger(2), "pdq");
        toAdd.put(createNewInteger(3), "xyz");


        final Map<Integer, String> expectedAll = new HashMap<>();
        expectedAll.put(createNewInteger(1), "xyz");
        expectedAll.put(createNewInteger(2), "pdq");
        expectedAll.put(createNewInteger(3), "xyz");
        expectedAll.put(createNewInteger(4), "ghi");
        expectedAll.put(createNewInteger(5), "jkl");

        final Map<Integer, String> expectedStrong = new HashMap<>();
        expectedStrong.put(createNewInteger(1), "xyz");
        expectedStrong.put(createNewInteger(2), "pdq");
        expectedStrong.put(createNewInteger(3), "xyz");
        expectedStrong.put(createNewInteger(4), "ghi");

        final TepidMap<Integer, String> map = new TepidMap<>();
        map.put(createNewInteger(1), "abc", true);
        map.put(createNewInteger(2), "def");
        map.put(createNewInteger(4), "ghi", true);
        map.put(createNewInteger(5), "jkl");
        map.putAll(toAdd, true);

        assertThat(map, is(expectedAll));

        System.gc();

        await().atMost(10, TimeUnit.SECONDS).until(()->map, Matchers.is(expectedStrong));
    }

    @Test
    public void shouldMergeWeakAndStrongKeysFromOtherTepidMap() {
        final TepidMap<Integer, String> toAdd = new TepidMap<>();
        toAdd.put(createNewInteger(1), "xyz");
        toAdd.put(createNewInteger(2), "pdq", true);
        toAdd.put(createNewInteger(3), "xyz", true);


        final Map<Integer, String> expectedAll = new HashMap<>();
        expectedAll.put(createNewInteger(1), "xyz");
        expectedAll.put(createNewInteger(2), "pdq");
        expectedAll.put(createNewInteger(3), "xyz");
        expectedAll.put(createNewInteger(4), "ghi");
        expectedAll.put(createNewInteger(5), "jkl");

        final Map<Integer, String> expectedStrong = new HashMap<>();
        expectedStrong.put(createNewInteger(2), "pdq");
        expectedStrong.put(createNewInteger(3), "xyz");
        expectedStrong.put(createNewInteger(4), "ghi");

        final TepidMap<Integer, String> map = new TepidMap<>();
        map.put(createNewInteger(1), "abc", true);
        map.put(createNewInteger(2), "def");
        map.put(createNewInteger(4), "ghi", true);
        map.put(createNewInteger(5), "jkl");
        map.putAll(toAdd);

        assertThat(map, is(expectedAll));

        System.gc();

        await().atMost(10, TimeUnit.SECONDS).until(()->map, Matchers.is(expectedStrong));
    }

    @Test
    public void shouldSumStrongAndWeakSizes() {
        final Map<Integer, Integer> map = new TepidMap<>(Maps.toIndexMap(1, 2, 3));
        map.put(9, 8);
        map.put(7, 6);
        assertThat(map.size(), is(5));
    }

    @Test
    public void shouldBeEmptyIfStrongAndWeakAreEmpty() {
        final TepidMap<String, String> map = new TepidMap<>();
        map.put("a", "b", true);
        assertThat(map.isEmpty(), is(false));
        map.remove("a");
        assertThat(map.isEmpty(), is(true));
        map.put("a", "b", false);
        assertThat(map.isEmpty(), is(false));
    }

    @Test
    public void shouldContainKeyIfStrongOrWeakDoes() {
        final TepidMap<String, String> map = new TepidMap<>();
        map.put("a", "b", true);
        assertThat(map.containsKey("a"), is(true));
        map.remove("a");
        assertThat(map.containsKey("a"), is(false));
        map.put("a", "b");
        assertThat(map.containsKey("a"), is(true));
    }

    @Test
    public void shouldContainValueIfStrongOrWeakDoes() {
        final TepidMap<String, String> map = new TepidMap<>();
        map.put("a", "b", true);
        assertThat(map.containsValue("b"), is(true));
        map.remove("a");
        assertThat(map.containsValue("b"), is(false));
        map.put("a", "b");
        assertThat(map.containsValue("b"), is(true));
    }

    @Test
    public void shouldClearWeakKeys() {
        final TepidMap<String, String> map = new TepidMap<>();
        map.put("a", "b");
        map.clear();
        assertThat(map.containsKey("a"), is(false));
    }

    @Test
    public void shouldClearStrongKeys() {
        final TepidMap<String, String> map = new TepidMap<>();
        map.put("a", "b", true);
        map.clear();
        assertThat(map.containsKey("a"), is(false));
    }

    @Test
    public void shouldStringifyStrongAndWeakContents() {
        final TepidMap<String, String> map = new TepidMap<>();
        map.put("a", "b", true);
        map.put("c", "d");
        assertThat(map.toString(), is("TepidMap{strong={a=b}, weak={c=d}}"));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void shouldNotBeEqualIfNull() {
        assertThat(new TepidMap<String, String>().equals(null), is(false));
    }

    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    @Test
    public void shouldNotBeEqualIfNotAMap() {
        assertThat(new TepidMap<String, String>().equals(new HashMap<>().keySet()), is(false));
    }

    @SuppressWarnings({"EqualsWithItself", "MismatchedQueryAndUpdateOfCollection"})
    @Test
    public void shouldBeEqualIfSameInstance() {
        final TepidMap<String, String> map = new TepidMap<>();
        assertThat(map.equals(map), is(true));
    }

    @Test
    public void shouldHashcodeFullEntrySet() {
        final TepidMap<String, String> map = new TepidMap<>();
        map.put("a", "b", true);
        map.put("c", "d");
        final Map<String, String> expected = new HashMap<>();
        expected.put("a", "b");
        expected.put("c", "d");
        assertThat(map.hashCode(), is(expected.entrySet().hashCode()));
    }


    @SuppressWarnings("UnnecessaryBoxing")
    private Integer createNewInteger(final int i) {
        return new Integer(i);
    }
}
