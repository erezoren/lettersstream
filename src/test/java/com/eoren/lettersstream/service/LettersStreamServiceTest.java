package com.eoren.lettersstream.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LettersStreamServiceTest {

  private LettersStreamService service;

  @BeforeEach
  void setUp() {
    service = new LettersStreamService();
  }

  @Test
  void testSetWordInitializesState() {
    service.setWord("abc");
    assertThat(service.isSubSequence("a")).isFalse();
    assertThat(service.isSubSequence("b")).isFalse();
    assertThat(service.isSubSequence("c")).isTrue();
  }

  @Test
  void testIsSubSequenceReturnsTrueWhenMatch() {
    service.setWord("xyz");
    assertThat(service.isSubSequence("x")).isFalse();
    assertThat(service.isSubSequence("y")).isFalse();
    assertThat(service.isSubSequence("z")).isTrue();
  }

  @Test
  void testIsSubSequenceReturnsFalseWhenNoMatch() {
    service.setWord("abc");
    service.isSubSequence("a");
    service.isSubSequence("b");
    assertThat(service.isSubSequence("x")).isFalse();
  }

  @Test
  void testBufferSlidingWindow() {
    service.setWord("abc");
    service.isSubSequence("a");
    service.isSubSequence("b");
    service.isSubSequence("c");
    assertThat(service.isSubSequence("d")).isFalse();
    assertThat(service.isSubSequence("e")).isFalse();
    assertThat(service.isSubSequence("a")).isFalse();
  }

  @Test
  void testMultipleMatches() {
    service.setWord("ab");
    assertThat(service.isSubSequence("a")).isFalse();
    assertThat(service.isSubSequence("b")).isTrue();
    assertThat(service.isSubSequence("a")).isFalse();
    assertThat(service.isSubSequence("b")).isTrue();
  }

  @Test
  void testResetClearsBuffer() {
    service.setWord("ab");
    service.isSubSequence("a");
    service.reset();
    assertThat(service.isSubSequence("b")).isFalse();
  }

  @Test
  void testSetWordChangesWindowSize() {
    service.setWord("ab");
    service.isSubSequence("a");
    service.setWord("xyz");
    assertThat(service.isSubSequence("x")).isFalse();
    assertThat(service.isSubSequence("y")).isFalse();
    assertThat(service.isSubSequence("z")).isTrue();
  }

  @Test
  void testSingleLetterWord() {
    service.setWord("q");
    assertThat(service.isSubSequence("q")).isTrue();
    assertThat(service.isSubSequence("a")).isFalse();
    assertThat(service.isSubSequence("q")).isTrue();
  }

  @Test
  void testContinuousInput() {
    service.setWord("xxx");
    String[] inputs = {"y", "x", "x", "x", "x", "t", "y", "x", "x"};
    boolean[] expected = {false, false, false, true, true, false, false, false, false};

    for (int i = 0; i < inputs.length; i++) {
      assertThat(service.isSubSequence(inputs[i]))
          .as("Input %s at index %d", inputs[i], i)
          .isEqualTo(expected[i]);
    }
  }

  @Test
  void testContinuousInput2() {
    service.setWord("xyz");
    String[] inputs = {"y", "a", "a", "x", "z", "x", "y", "z", "z"};
    boolean[] expected = {false, false, false, false, false, false, false, true, false};

    for (int i = 0; i < inputs.length; i++) {
      assertThat(service.isSubSequence(inputs[i]))
          .as("Input %s at index %d", inputs[i], i)
          .isEqualTo(expected[i]);
    }
  }

  @Test
  void testEmptyWord() {
    service.setWord("");
    assertThat(service.isSubSequence("a")).isFalse();
  }
}