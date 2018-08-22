package katas;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class FizzBuzzTest {
    @Test
    public void shouldReturnNumberForUnmatched() {
        assertThat(ModReplacer.apply(0), is("0"));
        assertThat(ModReplacer.apply(1), is("1"));
        assertThat(ModReplacer.apply(2), is("2"));
        assertThat(ModReplacer.apply(4), is("4"));
    }

    @Test
    public void shouldReturnFizzForMultiplesOfThree() {
        assertThat(ModReplacer.apply(3), is("Fizz"));
        assertThat(ModReplacer.apply(6), is("Fizz"));
        assertThat(ModReplacer.apply(9), is("Fizz"));
        assertThat(ModReplacer.apply(12), is("Fizz"));
        assertThat(ModReplacer.apply(18), is("Fizz"));
    }

    @Test
    public void shouldReturnBuzzForMulitplesOf5() {
        assertThat(ModReplacer.apply(5), is("Buzz"));
        assertThat(ModReplacer.apply(10), is("Buzz"));
        assertThat(ModReplacer.apply(20), is("Buzz"));
        assertThat(ModReplacer.apply(25), is("Buzz"));
        assertThat(ModReplacer.apply(35), is("Buzz"));
    }

    @Test
    public void shouldReturnFizzBuzzForMultiplesOf15() {
        assertThat(ModReplacer.apply(15), is("FizzBuzz"));
        assertThat(ModReplacer.apply(30), is("FizzBuzz"));
        assertThat(ModReplacer.apply(45), is("FizzBuzz"));
    }


    public static class ModReplacer {
        static String apply(int i) {
            String result = "";
            if (i > 0 && i % 3 == 0) result += "Fizz";
            if (i > 0 && i % 5 == 0) result += "Buzz";
            if (result.isEmpty()) result = String.valueOf(i);
            return result;
        }
    }
}
