package com.eoren.lettersstream.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class WordServiceTest {

  private ApplicationEventPublisher publisher;
  private WordService wordService;

  @BeforeEach
  void setUp() {
    publisher = mock(ApplicationEventPublisher.class);
    wordService = new WordService(publisher);
  }

  @Test
  void setWord_shouldStoreWordAndPublishEvent() {
    wordService.setWord("hello");

    assertThat(wordService.getWord()).isEqualTo("hello");

    ArgumentCaptor<WordChangedEvent> eventCaptor = ArgumentCaptor.forClass(WordChangedEvent.class);
    verify(publisher).publishEvent(eventCaptor.capture());
    WordChangedEvent event = eventCaptor.getValue();
    assertThat(event.getNewWord()).isEqualTo("hello");
  }

  @Test
  void getWord_shouldReturnNullInitially() {
    assertThat(wordService.getWord()).isNull();
  }
}