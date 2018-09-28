package katas;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class FactorialTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

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

    @Test
    public void shouldNotCreate() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        thrown.expect(InvocationTargetException.class);
        thrown.expectCause(Matchers.isA(UnsupportedOperationException.class));
        final Constructor c = Factorial.class.getDeclaredConstructor();
        c.setAccessible(true);
        c.newInstance();
    }

}
