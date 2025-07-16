package com.eoren.lettersstream.service.event;

import org.springframework.context.ApplicationEvent;

public class WordChangedEvent extends ApplicationEvent {
  private final String newWord;

  public WordChangedEvent(Object source, String newWord) {
    super(source);
    this.newWord = newWord;
  }

  public String getNewWord() {
    return newWord;
  }
}
