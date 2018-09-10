package katas.resources;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Resources {
    public static List<String> readResource(final String resourceName) {
        final List<String> results = new ArrayList<>();
        readResource(resourceName, results::add);
        return results;
    }

    public static void readResource(final String resourceName, final Consumer<String> consumer) {
        try (final InputStream resourceStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);
             final BufferedReader reader = new BufferedReader(new InputStreamReader(resourceStream))) {
            reader.lines().forEach(consumer);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}
