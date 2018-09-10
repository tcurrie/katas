package katas.dictionary;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.function.Consumer;

import katas.resources.Resources;
import org.junit.Test;

public class Dictionary {
    private static final String RESOURCE_NAME = "wordlist.txt";

    public static List<String> readFile() {
        return Resources.readResource(RESOURCE_NAME);
    }

    public static void readWords(final Consumer<String> consumer) {
        Resources.readResource(RESOURCE_NAME, consumer);
    }

    @Test
    public void shouldReadWordList() {
        assertThat(readFile().size(), is(338_882));
    }

}
