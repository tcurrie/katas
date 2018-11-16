package katas.twentyfour;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import katas.Combinations;
import katas.equations.Equation;
import katas.equations.Operation;
import katas.equations.Operator;
import katas.equations.Value;
import org.junit.Test;

public class TwentyFourTest {
    public static final Collector<Equation, ?, Map<Double, Set<Equation>>> EQUATION_RESULT_COLLECTOR = Collectors.groupingBy(e -> (double) Math.round(e.getResult() * 1_000_000) / 1_000_000, Collectors.toSet());
    //  q1 Using ALL of 1, 3, 4, 6 and ANY of + - * / create an equation to get the result 24.
    //  q2 Using 3, 4, 7, 8 get 10
    //  q3 Add operators ^ and %.
    //  q4 Find all equations for 4 distinct integers 0-9 that are the only solution that produces an integer result between 1 and 100
    // 1 3 4 6
    // + - * /

    @Test
    public void shouldGetTwentyFour() {
        final Set<Equation> equations = createEquations(
                asList(1d, 3d, 4d, 6d),
                asList(Operator.ADDITION, Operator.SUBTRACTION, Operator.MULTIPLICATION, Operator.DIVISION))
            .filter(e->e.getResult()==24.0)
            .collect(Collectors.toSet());

        assertThat(equations, is(new HashSet<>(singletonList(
            Operation.of(
                Value.of(6),
                Operation.of(
                    Value.of(1),
                    Operation.of(Value.of(3), Value.of(4), Operator.DIVISION),
                    Operator.SUBTRACTION),
                Operator.DIVISION)
        ))));
    }


    @Test
    public void interestingEquationsFrom0to10Choose4() {
        final Set<Equation> equations = Combinations.getDistinctCombinations(0, 10, 4).parallel()
                .flatMap(values->
                    createEquations(values, asList(Operator.ADDITION, Operator.SUBTRACTION, Operator.MULTIPLICATION, Operator.DIVISION))
                        .collect(EQUATION_RESULT_COLLECTOR)
                        .entrySet().stream()
                        .filter(e->e.getValue().size()==1)
                        .filter(e->e.getKey()==Math.round(e.getKey()))
                        .filter(e->e.getKey()>0)
                        .filter(e->e.getKey().intValue()<=100)
                        .flatMap(e->e.getValue().stream())
                ).collect(Collectors.toSet());
        assertThat(equations.size(), is(1599));
//        equations.stream().sorted(Comparator.comparingDouble(Equation::getResult)).forEach(e-> System.out.println(Math.round(e.getResult()) + "=" + e));
    }

    @Test
    public void interestingAnswersFrom0to10Choose4() {
        final Set<Equation> equations = Combinations.getDistinctCombinations(0, 10, 4).parallel()
            .flatMap(values->
                createEquations(values, asList(Operator.ADDITION, Operator.SUBTRACTION, Operator.MULTIPLICATION, Operator.DIVISION))
                    .collect(EQUATION_RESULT_COLLECTOR)
                    .entrySet().stream()
                    .filter(e->e.getValue().size()==1)
                    .filter(e->e.getKey()==Math.round(e.getKey()))
                    .filter(e->e.getKey()>0)
                    .filter(e->e.getKey().intValue()<=100)
                    .flatMap(e->e.getValue().stream())
            ).collect(EQUATION_RESULT_COLLECTOR)
            .entrySet().stream()
            .sorted(Comparator.comparingInt(e->e.getValue().size()))
            .limit(10)
//            .map(e->{ System.out.println(e.getKey() + " [" + e.getValue().size() + "] = " + e.getValue()); return e; })
            .flatMap(e->e.getValue().stream())
            .collect(Collectors.toSet());

        final Set<Double> results = equations.stream().map(e->e.getResult()).collect(Collectors.toSet());
        assertThat(results, is(
            new HashSet<>(Arrays.asList(2d, 4d, 5d, 6d, 7d, 8d, 10d, 12d, 16d, 18d))
            ));
    }


    private Stream<Equation> createEquations(final Collection<Double> values, final Collection<Operator> operators) {
        final List<BiFunction<Equation, Equation, Equation>> equationFactories = operators.stream()
            .flatMap(Operation::factoriesOf).collect(Collectors.toList());

        return Combinations.getDistinctPermutations(values).flatMap(v ->
            Combinations.getAllPermutations(equationFactories, values.size() - 1).map(ef -> {
                Equation equation = Value.of(v.get(0));
                for (int i = 1; i < v.size(); i++) {
                    equation = ef.get(i-1).apply(equation, Value.of(v.get(i)));
                }
                return equation;
            })
        );
    }
}
