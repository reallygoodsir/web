package org.query.filter;

import java.util.ArrayList;
import java.util.List;

public class WordsFilter {

    public List<String> filter(List<String> words, String word, int length) {
        List<String> result = new ArrayList<>();
        for (String line : words) {
            if (line.length() <= length && line.contains(word)) {
                result.add(line);
            }
        }
        return result;
    }
}
