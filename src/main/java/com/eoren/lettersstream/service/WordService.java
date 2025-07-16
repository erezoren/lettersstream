package com.eoren.lettersstream.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WordService {

  private final ApplicationEventPublisher publisher;
  private String word;

  public void setWord(String word) {
    this.word = word;
    publisher.publishEvent(new WordChangedEvent(this, word));
  }

  public String getWord() {
    return word;
  }
}
