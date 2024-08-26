package org.query.model;

public class JavaWord {
    private String word;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public String toString() {
        return "JavaWord{" +
                "word='" + word + '\'' +
                '}';
    }
}
