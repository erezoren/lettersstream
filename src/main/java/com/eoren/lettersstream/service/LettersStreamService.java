package com.eoren.lettersstream.service;

import java.util.ArrayDeque;
import java.util.Deque;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LettersStreamService {

  private int windowSize;
  private Deque<Character> buffer;
  private String word;

  public void setWord(String word) {
    this.word = word;
    windowSize = word.length();
    buffer = new ArrayDeque<>();
  }

  public boolean isSubSequence(String letter) {
    if (wordMissing(letter)) {
      return false;
    }
    buffer.addLast(letter.charAt(0));
    if (buffer.size() > windowSize) {
      buffer.removeFirst();
    }

    if (buffer.size() == windowSize) {
      StringBuilder current = new StringBuilder();
      for (char c : buffer) {
        current.append(c);
      }
      return current.toString().equals(word);
    }

    return false;
  }

  private boolean wordMissing(String letter) {
    return word == null || word.isEmpty() || letter == null || letter.isEmpty();
  }

  public void reset() {
    buffer.clear();
  }
}
