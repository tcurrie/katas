package katas.conflictingobjectives;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TDDSixLetterWordPairFinder implements WordGroupFinder {
    public Set<List<String>> find(final List<String> dictionary) {
        final Set<List<String>> pairs = new HashSet<>();
        if (dictionary == null || dictionary.isEmpty()) {
            return pairs;
        }

        Set<String> sixLetterWords = new HashSet<>();
        final List<String> shortWords = new ArrayList<>();
        for (final String word : dictionary) {
            if (word.length() == 6) {
                sixLetterWords.add(word);
            } else if (word.length() < 6){
                shortWords.add(word);
            }
        }

        for (final String wordOne : shortWords) {
            for (final String wordTwo : shortWords) {
                if (sixLetterWords.contains(wordOne + wordTwo)) {
                    pairs.add(Arrays.asList(wordOne, wordTwo));
                }
            }
        }
        return pairs;
    }
}
