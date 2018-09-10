package katas;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class EncoderTest {
    private static final Encoder<Integer> FIZZ_BUZZ =
        ModEncoder.when(3).then("Fizz")
            .append(ModEncoder.when(5).then("Buzz"))
            .or(Encoder.STRING);

    @Test
    public void shouldFizzBuzz() {
        assertThat(FIZZ_BUZZ.encode(1), is("1"));
        assertThat(FIZZ_BUZZ.encode(2), is("2"));
        assertThat(FIZZ_BUZZ.encode(3), is("Fizz"));
        assertThat(FIZZ_BUZZ.encode(4), is("4"));
        assertThat(FIZZ_BUZZ.encode(5), is("Buzz"));
        assertThat(FIZZ_BUZZ.encode(6), is("Fizz"));
        assertThat(FIZZ_BUZZ.encode(15), is("FizzBuzz"));
    }


    public interface Encoder<T> {
        Encoder<Object> STRING = String::valueOf;
        String encode(T value);

        interface Builder<T> {
            Encoder<T> then(String name);
        }

        default Encoder<T> append(final Encoder<T> next) {
            return value -> this.encode(value) + next.encode(value);
        }

        default Encoder<T> or(final Encoder<? super T> alternative) {
            return i -> {
                final String result = this.encode(i);
                return result.isEmpty() ? alternative.encode(i) : result;
            };
        }
    }

    public interface ModEncoder extends Encoder<Integer> {
        static Builder<Integer> when(final int match) {
            return name -> i -> i != 0 && i % match == 0 ? name : "";
        }
    }
}
