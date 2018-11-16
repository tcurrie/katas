package katas.conflictingobjectives;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public class PerformantSixLetterWordPairFinder implements WordGroupFinder {

    public Set<List<String>> find(final List<String> dictionary) {
        if (dictionary == null || dictionary.isEmpty()) {
            return new HashSet<>();
        }

        final List<Map> s = IntStream.range(0, 6).boxed().map(i->new HashMap<>()).collect(Collectors.toList());
        for (final String word : dictionary) {
            if (word.length() <= 6) {
                mapBytes(s.get(word.length() - 1), word.toLowerCase().getBytes(Charset.defaultCharset()));
            }
        }

        return (Set<List<String>>) IntStream.of(1, 2, 3, 4, 5).boxed()
            .flatMap(i->match(0, 0, new byte[][]{new byte[i], new byte[6 - i]}, Collections.singletonList(s.get(5 - i)), s.get(i - 1), s.get(5)))
            //.map(l->{System.out.println(l);return l;})
            .collect(Collectors.toSet());
    }

    @SuppressWarnings("unchecked")
    private Stream<List<String>> match(final int word, final int letter, final byte[][] words, final List<Map<Byte, Map<?, ?>>> matchFrom, final Map<Byte, Map<?, ?>> matchCurrent, final Map<Byte, Map<?, ?>> matchTo) {
//        System.out.println(word + ":" + words.length + ", " + letter + ":" + words[word].length  + ", " + Arrays.deepToString(words) + ", " + matchFrom + ", " + matchCurrent  + ", " + matchTo);
        if (word == words.length - 1 && letter == words[word].length) {
            return Stream.of(Arrays.stream(words).map(String::new).collect(Collectors.toList()));
        } else if (letter == words[word].length) {
            return match(word + 1, 0, words, matchFrom, matchFrom.get(word), matchTo);
        } else {
            return matchCurrent.entrySet().stream().flatMap(e->{
                final Map<Byte, Map<?, ?>> nextWordByte = (Map<Byte, Map<?, ?>>) matchTo.get(e.getKey());
                if (nextWordByte != null) {
                    words[word][letter] = e.getKey();
                    return match(word, letter + 1, words, matchFrom, (Map<Byte, Map<?, ?>>) e.getValue(), nextWordByte);
                } else {
                    return Stream.empty();
                }
            });
        }
    }

    @SuppressWarnings("unchecked")
    private void mapBytes(final Map<?, ?> words, final byte[] bytes) {
        Map container = words;
        for (int i = 0; i < bytes.length; i++) {
            container.putIfAbsent(bytes[i], new HashMap<>());
            container = (Map) container.get(bytes[i]);
        }
    }
}
