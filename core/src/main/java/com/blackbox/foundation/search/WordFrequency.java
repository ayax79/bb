package com.blackbox.foundation.search;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author A.J. Wright
 */
@XmlRootElement
public class WordFrequency implements Serializable {

    private int frequency;
    private String word;

    public WordFrequency() {
    }

    public WordFrequency(String word, int frequency) {
        this.word = word;
        this.frequency = frequency;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WordFrequency that = (WordFrequency) o;

        if (frequency != that.frequency) return false;
        //noinspection RedundantIfStatement
        if (word != null ? !word.equals(that.word) : that.word != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = frequency;
        result = 31 * result + (word != null ? word.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "WordFrequency{" +
                "frequency=" + frequency +
                ", word='" + word + '\'' +
                '}';
    }
}
