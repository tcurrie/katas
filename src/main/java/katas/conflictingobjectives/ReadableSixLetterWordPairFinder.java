package katas.conflictingobjectives;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReadableSixLetterWordPairFinder implements WordGroupFinder {
    public Set<List<String>> find(final List<String> dictionary) {
        if (dictionary == null || dictionary.isEmpty())
            return new HashSet<>();

        final Set<String> lowerCaseWords = mapToLowercaseWordSet(dictionary);
        final Set<String> sixLetterWords = filterWords(lowerCaseWords, w -> w.length() == 6);
        final Set<String> shorterWords = filterWords(lowerCaseWords, w -> w.length() < 6);

        return generateWordPairStream(shorterWords)
                .filter(pair->sixLetterWords.contains(pair.get(0) + pair.get(1)))
                .collect(Collectors.toSet());
    }

    private Stream<List<String>> generateWordPairStream(final Set<String> shorterWords) {
        return shorterWords.stream().flatMap(firstWord->shorterWords.stream().map(secondWord-> Arrays.asList(firstWord, secondWord)));
    }

    private Set<String> mapToLowercaseWordSet(final List<String> dictionary) {
        return dictionary.stream().map(String::toLowerCase).collect(Collectors.toSet());
    }

    private Set<String> filterWords(final Set<String> dictionary, final Predicate<String> includeIf) {
        return dictionary.stream().filter(includeIf).collect(Collectors.toSet());
    }
}
