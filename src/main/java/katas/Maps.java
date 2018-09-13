package katas;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Maps {
    private Maps() { }

    public static <T> Map<Integer, T> toIndexMap(final T... data) {
        final AtomicInteger index = new AtomicInteger();
        return Arrays.stream(data).collect(Collectors.toMap(v->index.getAndIncrement(), v->v, (a, b)->a));
    }

    public static <A, B, C, D> Map<C, D> adapt(final Map<A, B> initial, Function<A, C> keyMapper, Function<B, D> valueMapper) {
        return initial.entrySet().stream().collect(Collectors.toMap(e->keyMapper.apply(e.getKey()), e->valueMapper.apply(e.getValue())));
    }
}
