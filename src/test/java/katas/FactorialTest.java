package katas;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

public class FactorialTest {
    @Test
    public void shouldReturnZeroForZeroFunction() {
        validate(0, 1);
    }

    @Test
    public void shouldReturnOnrForOnrFunction() {
        validate(1, 1);
    }

    @Test
    public void shouldReturnTwoForTwoFunction() {
        validate(2, 2);
    }

    @Test
    public void shouldReturnSixForThreeFunction() {
        validate(3, 6);
    }

    @Test
    public void shouldReturn24For4Function() {
        validate(4, 24);
    }

    @Test
    public void shouldReturn120For5Function() {
        validate(5, 120);
    }

    @Test
    public void shouldReturn720For6Function() {
        validate(6, 720);
    }

    private void validate(final int input, final int expected) {
        Assert.assertThat(Factorial.CALC.apply(input), CoreMatchers.is(expected));
    }
}
