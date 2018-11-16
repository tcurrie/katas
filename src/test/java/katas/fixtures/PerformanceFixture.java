package katas.fixtures;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class PerformanceFixture {


    public static <T, U extends Collection<T>> List<double[]> getMedianScaledExecutionTimes(final Consumer<U> unitToTest, final Function<Long, U> scaleDataSet, final int maxScale, final int samples) {

        return  IntStream.range(0, samples).boxed()
            .flatMap(i ->LongStream.range(1, maxScale + 1).boxed()
                .map(sequence-> {
                    final U data = scaleDataSet.apply(sequence);
                    return new double[]{((double)data.size()), getExecutionTime(unitToTest, data)};
                }))
            .collect(Collectors.toList());
    }

    private static <T> Long getExecutionTime(final Consumer<T> unitToTest, final T data) {
        final long start = System.nanoTime();
        unitToTest.accept(data);
        final long end = System.nanoTime();
        return end - start;
    }

    private static double getMedian(final List<Double> samples) {
        return samples.stream().sorted().mapToDouble(d->d)
            .skip(samples.size()%2 == 0 ? samples.size()/2-1 : samples.size()/2)
            .limit(samples.size()%2 == 0 ? 2 : 1)
            .average()
            .orElseThrow(UnsupportedOperationException::new);
    }

    public static double getMedianMedianSlopeByScale(final List<double[]> results) {
        //final Map<Double, List<double[]>> m =
        return getMedian(results.stream()
            .collect(Collectors.toMap(r->r[0], r -> Stream.of((Double)r[1]), Stream::concat))
            .entrySet().stream()
            .map(e->getMedian(e.getValue()
                .map(rise->rise/e.getKey())
                .collect(Collectors.toList())))
            .collect(Collectors.toList()));
    }

    public static double getRSquared(final List<double[]> results, final Function<Double, Double> f) {
        final double meanY =
            results.stream()
                .map(r->r[1])
                .mapToDouble(d->d)
                .average().orElse(0);

        final double ssTot = results.stream()
            .map(r->Math.pow(r[1] - meanY, 2))
            .mapToDouble(d->d)
            .sum();
        final double ssRes = results.stream()
            .map(r->Math.pow(r[1] - f.apply(r[0]), 2))
            .mapToDouble(d->d)
            .sum();
        double rSquared = 1 - ssRes/ssTot;
/*        System.out.println(meanY);
        System.out.println(ssTot);
        System.out.println(ssRes);
        System.out.println(rSquared);*/
        return rSquared;
    }

}
