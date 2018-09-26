package katas;

import static org.junit.Assert.assertThat;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.stream.LongStream;

import org.hamcrest.Matchers;
import org.junit.Test;

public class PrimesTest {
    @Test
    public void given0ThenEmpty() {
        validate(0);
    }
    @Test
    public void given1ThenEmpty() {
        validate(1);
    }

    @Test
    public void given2isPrime() {
        validate(2, 2);
    }

    @Test
    public void givenMultiplesOf2ThenReduceToListOf2s() {
        validate(4, 2, 2);
        validate(8, 2, 2, 2);
        validate(16, 2, 2, 2, 2);
        validate(32, 2, 2, 2, 2, 2);
    }

    @Test
    public void given3isPrime() {
        validate(3, 3);
    }

    @Test
    public void givenMultiplesOf3ThenReduceToListOf3s() {
        validate(9, 3, 3);
        validate(27, 3, 3, 3);
        validate(81, 3, 3, 3, 3);
        validate(243, 3, 3, 3, 3, 3);
    }

    @Test
    public void givenMultiplesOf2and3ThenReduceToListOf2sAnd3s() {
        validate(6, 2, 3);
        validate(12, 2, 2, 3);
        validate(18, 2, 3, 3);
        validate(24, 2, 2, 2, 3);
        validate(36, 2, 2, 3, 3);
        validate(48, 2, 2, 2, 2, 3);
        validate(54, 2, 3, 3, 3);
    }

    @Test
    public void given5isPrime() {
        validate(5, 5);
    }

    @Test
    public void givenMultiplesOf6Over5arePrime() {
        validate(11, 11);
        validate(17, 17);
        validate(23, 23);
        validate(29, 29);


        validate(47, 47);
        validate(53, 53);
        validate(59, 59);
    }

    @Test
    public void given7isPrime() {
        validate(7, 7);
    }

    @Test
    public void givenMultiplesOf6Over7arePrime() {
        validate(13, 13);
        validate(19, 19);

        validate(31, 31);
        validate(37, 37);
        validate(43, 43);


        validate(61, 61);
        validate(67, 67);
    }

    @Test
    public void givenLargeSetOfPrimesMultipliedThenReduceToPrimes() {
        validate(multiplyValues(2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37),
            2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37);
    }

    @Test
    public void givenLargerSetOfPrimesMultipliedThenReduceToPrimes() {
        validate(multiplyValues(43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109),
            43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109);
    }

    @Test
    public void millionthPrime() {
        validate(15485863, 15485863);
    }

    @Test
    public void tenthMillionPrime() {
        validate(179424673, 179424673);
    }

    @Test
    public void fiftiethMillionPrime() {
        validate(982451653, 982451653);
    }

    @Test
    public void mersennePrimes() {
        validateMersennePrime("Mersenne1", 2);
        validateMersennePrime("Mersenne2", 3);
        validateMersennePrime("Mersenne3", 5);
        validateMersennePrime("Mersenne4", 7);
        validateMersennePrime("Anonymous", 13);
        validateMersennePrime("Cataldi1", 17);
        validateMersennePrime("Cataldi2", 19);
        validateMersennePrime("Euler", 31);
    }

    private void validateMersennePrime(final String discoverer, final int exponent) {
        final BigInteger mersennne = BigInteger.valueOf(2).pow(exponent).subtract(BigInteger.ONE);
        assertThat("Discovered by " + discoverer, Primes.factors(mersennne), Matchers.containsInAnyOrder(mersennne));
    }

    private BigInteger multiplyValues(final long... values) {
        return Arrays.stream(values).boxed()
            .map(BigInteger::valueOf)
            .reduce(BigInteger.ONE, BigInteger::multiply);
    }

    private void validate(final long input, final long... expected) {
        validate(BigInteger.valueOf(input), expected);
    }

    private void validate(final BigInteger input, final long... expected) {
        assertThat(Primes.factors(input), Matchers.containsInAnyOrder(LongStream.of(expected).boxed().map(BigInteger::valueOf).toArray(BigInteger[]::new)));
    }

}
