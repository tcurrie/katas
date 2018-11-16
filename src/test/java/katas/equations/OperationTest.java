package katas.equations;

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Enclosed.class)
public class OperationTest {

    public static class Factory {
        @Test
        public void shouldCreateSingleFactoryForCommutativeOperation() {
            final List<Equation> equations = Operation.factoriesOf(Operator.ADDITION)
                .map(f->f.apply(Value.of(2), Value.of(2)))
                .collect(Collectors.toList());

            assertThat(equations, is(singletonList(Operation.of(Value.of(2), Value.of(2), Operator.ADDITION))));
        }

        @Test
        public void shouldCreateLeftRightAndRightLeftFactoryForNonCommutativeOperation() {
            final List<Equation> equations = Operation.factoriesOf(Operator.DIVISION)
                .map(f->f.apply(Value.of(1), Value.of(2)))
                .collect(Collectors.toList());

            assertThat(equations, is(Arrays.asList(
                Operation.of(Value.of(1), Value.of(2), Operator.DIVISION),
                Operation.of(Value.of(2), Value.of(1), Operator.DIVISION)
            )));
        }

    }

    @RunWith(Parameterized.class)
    public static class OperationResults {
        @Parameterized.Parameter()
        public int left;
        @Parameterized.Parameter(1)
        public int right;
        @Parameterized.Parameter(2)
        public Operator operator;
        @Parameterized.Parameter(3)
        public double expected;

        @Parameterized.Parameters(name= "{index}: [{0}], [{1}], [{2}] = [{3}]")
        public static Object[][] data() {
            return new Object[][]{
                { 1, 1, Operator.ADDITION, 2 },
                { 1, 2, Operator.ADDITION, 3 },
                { -2, -1, Operator.ADDITION, -3 },
                { 10, 3, Operator.SUBTRACTION, 7 },
                { 3, 10, Operator.SUBTRACTION, -7 },
                { 23, 13, Operator.SUBTRACTION, 10 },
                { 3, 7, Operator.MULTIPLICATION, 21 },
                { -5, -5, Operator.MULTIPLICATION, 25 },
                { 20, 4, Operator.DIVISION, 5 },
                { -30, 12, Operator.DIVISION, -2.5 },
            };
        }

        @Test
        public void shouldGetExpectedResult() {
            assertThat(Operation.of(Value.of(left), Value.of(right), operator).getResult(), is(expected));
        }

        @Test
        public void shouldGetExpectedString() {
            assertThat(Operation.of(Value.of(left), Value.of(right), operator).toString(), is(String.format("(%s %s %s)", left, operator.getSymbol(), right)));
        }
    }

    public static class EqualsAndHashCodeTest {
        @Test
        public void sameValuesCommutativeOperationAreEqual() {
            final Equation a = Operation.of(Value.of(1), Value.of(2), Operator.ADDITION);
            final Equation b = Operation.of(Value.of(1), Value.of(2), Operator.ADDITION);

            assertThat(a, is(b));
            assertThat(a.hashCode(), is(b.hashCode()));
        }

        @Test
        public void sameValuesNonCommutativeOperationAreEqual() {
            final Equation a = Operation.of(Value.of(1), Value.of(2), Operator.SUBTRACTION);
            final Equation b = Operation.of(Value.of(1), Value.of(2), Operator.SUBTRACTION);

            assertThat(a, is(b));
            assertThat(a.hashCode(), is(b.hashCode()));
        }

        @Test
        public void reverseValuesCommutativeOperationAreEqual() {
            final Equation a = Operation.of(Value.of(1), Value.of(2), Operator.ADDITION);
            final Equation b = Operation.of(Value.of(2), Value.of(1), Operator.ADDITION);

            assertThat(a, is(b));
            assertThat(a.hashCode(), is(b.hashCode()));
        }

        @Test
        public void reverseValuesNonCommutativeOperationAreNotEqual() {
            final Equation a = Operation.of(Value.of(1), Value.of(2), Operator.SUBTRACTION);
            final Equation b = Operation.of(Value.of(2), Value.of(1), Operator.SUBTRACTION);

            assertThat(a, is(not(b)));
            assertThat(a.hashCode(), is(not(b.hashCode())));
        }

        @Test
        public void differentValuesSameOperationAreNotEqual() {
            final Equation a = Operation.of(Value.of(1), Value.of(2), Operator.ADDITION);
            final Equation b = Operation.of(Value.of(3), Value.of(4), Operator.ADDITION);

            assertThat(a, is(not(b)));
            assertThat(a.hashCode(), is(not(b.hashCode())));
        }

        @Test
        public void sameValuesDifferentOperationAreNotEqual() {
            final Equation a = Operation.of(Value.of(1), Value.of(2), Operator.ADDITION);
            final Equation b = Operation.of(Value.of(1), Value.of(2), Operator.MULTIPLICATION);

            assertThat(a, is(not(b)));
            assertThat(a.hashCode(), is(not(b.hashCode())));
        }
    }



}
