package katas;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class LeapYearsTest {
    @Test
    public void yearsDivisibleBy4AreLeapYears() {
        validate(true, 4, 8, 12, 16, 96);
    }

    @Test
    public void yearsNotDivisibleBy4AreNotLeapYears() {
        validate(false, 1, 2, 3, 5, 6, 7, 9, 95, 97);
    }

    @Test
    public void yearsDivisibleBy100AreNotLeapYears() {
        validate(false, 100, 200, 300, 900);
    }

    @Test
    public void yearsDivisibleBy400AreLeapYears() {
        validate(true, 400, 800, 1200, 3600);
    }

    @Test
    public void yearsDivisibleBy4000AreNotLeapYears() {
        validate(false, 4000, 8000, 12000, 36000);
    }

    private void validate(final boolean expected, final int... years) {
        for (final int year : years) {
            assertThat(LeapYears.isLeapYear(year), is(expected));
        }
    }

}
