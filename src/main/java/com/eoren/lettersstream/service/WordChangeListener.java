package com.eoren.lettersstream.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WordChangeListener {

  private final LettersStreamService lettersStreamService;

  @EventListener
  public void onWordChanged(WordChangedEvent event) {
    lettersStreamService.setWord(event.getNewWord());
  }
}
