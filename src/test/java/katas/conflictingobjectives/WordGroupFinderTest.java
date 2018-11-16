package katas.conflictingobjectives;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import katas.fixtures.PerformanceFixture;
import org.junit.Assume;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class WordGroupFinderTest {

    @Parameterized.Parameters(name= "{index}: WordGroupFinder[{0}]")
    public static Iterable<? extends Object> data() {
        return Arrays.asList(
            new TDDSixLetterWordPairFinder(),
            new ReadableSixLetterWordPairFinder(),
            new PerformantSixLetterWordPairFinder()
        );
    }

    @Parameterized.Parameter
    public WordGroupFinder finder;

    @Test
    public void givenNullDictionaryReturnsEmptySet() {
        validate(null);
    }

    @Test
    public void givenEmptyDictionaryReturnsEmptySet() {
        validate(new ArrayList<>());
    }

    @Test
    public void givenSingleSetDictionaryReturnsTheSixLetterWordSet() {
        validate(Arrays.asList("al", "bums", "albums"), Arrays.asList("al", "bums"));
        validate(Arrays.asList("bar", "ely", "barely"), Arrays.asList("bar", "ely"));
    }

    @Test
    public void givenWordsThatDontMatchSetDictionaryReturnsEmptySet() {
        validate(Arrays.asList("ab", "bums", "albums"));
    }

    @Test
    public void givenWordsThatAreTooLongReturnsEmptySet() {
        validate(Arrays.asList("to", "tally", "totally"));
        validate(Arrays.asList("to", "tallycool", "totallycool"));
    }

    @Test
    public void givenWordsThatAreTooShortReturnsEmptySet() {
        validate(Arrays.asList("to", "tal", "total"));
        validate(Arrays.asList("to", "t", "tot"));
    }

    @Test
    public void givenShortWordsThatAreJustLongEnoughReturnsSet() {
        validate(Arrays.asList("album", "s", "albums"), Arrays.asList("album", "s"));
        validate(Arrays.asList("a", "lbums", "albums"), Arrays.asList("a", "lbums"));
    }

    @Test
    public void givenSixLetterWordInAnyPositionSetDictionaryInAnyOrderReturnsTheSixLetterWordSet() {
        validate(Arrays.asList("al", "bums", "albums"), Arrays.asList("al", "bums"));
        validate(Arrays.asList("al", "albums", "bums"), Arrays.asList("al", "bums"));
        validate(Arrays.asList("albums", "al", "bums"), Arrays.asList("al", "bums"));
    }
    @Test
    public void givenShortWordsInAnyOrderSetDictionaryReturnsTheSixLetterWordSet() {
        validate(Arrays.asList("al", "bums", "albums"), Arrays.asList("al", "bums"));
        validate(Arrays.asList("bums", "al", "albums"), Arrays.asList("al", "bums"));
    }

    @Test
    public void givenTwoSetDictionaryReturnsBothSixLetterWordSet() {
        validate(Arrays.asList("al", "bums", "albums", "bar", "ely", "barely"), Arrays.asList("al", "bums"), Arrays.asList("bar", "ely"));
    }

    private void validate(final List<String> dictionary, final List<String>... expected) {
        final Set<List<String>> sixLetterWordPairs = finder.find(dictionary);
        assertThat(sixLetterWordPairs, is(new HashSet<>(Arrays.asList(expected))));
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Ignore
    @Test
    public void givenLogarithmicGrowthOfDataSetRunsInLinearTime() {
        Assume.assumeThat(finder, is(instanceOf(PerformantSixLetterWordPairFinder.class)));

        finder.find(scaleDictionaryLinearly(16));

        final List<double[]> results = PerformanceFixture.getMedianScaledExecutionTimes(finder::find, this::scaleDictionaryLinearly, 1, 40);

        final double medianSlope = PerformanceFixture.getMedianMedianSlopeByScale(results);

        final Function<Double, Double> f = x -> x * medianSlope;
        double rSquared = PerformanceFixture.getRSquared(results, f);


        final Map<Double, List<Double>> m = results.stream().collect(Collectors.toMap(r-> r[0], r->new ArrayList<>(Arrays.asList(r[1])), (a, b)->{ a.addAll(b); Collections.sort(a); return a; }));
        assertThat("Failed to scale close to linear slope of [" + medianSlope + "]\n\t" +

            m.entrySet().stream()
                .sorted(Comparator.comparingDouble(Map.Entry::getKey))
                .map(e->
                    e.getKey() + "] expect [" + f.apply(e.getKey()) + "] got: med " +
                        e.getValue().get(e.getValue().size()/2) + " sl " +
                        e.getValue().get(e.getValue().size()/2) / e.getKey() + ", vals: " +
                        e.getValue()
                ).collect(Collectors.joining("\n\t"))

            ,
             rSquared, greaterThan(.1));
    }


    private List<String> scaleDictionaryLinearly(final long scale) {
//        return IntStream.range(0, 1_000_000).boxed().map(Object::toString).collect(Collectors.toList());
        return IntStream.range(0,8)
            .flatMap(i->IntStream.range((int)Math.pow(10,i), (int)Math.pow(10,i+scale/(scale + Math.pow(2,2*i)-1))))
            .boxed()
            .map(Object::toString)
            .collect(Collectors.toList());
    }



}
