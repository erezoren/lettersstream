package com.eoren.lettersstream.reasource;

import static com.eoren.lettersstream.model.Errors.LETTERS_MISSING_ERROR;
import static com.eoren.lettersstream.model.Errors.ONE_CHARACTER_ONLY_ERROR;
import static com.eoren.lettersstream.model.Errors.WORD_MISSING_ERROR;

import com.eoren.lettersstream.model.LetterResponse;
import com.eoren.lettersstream.service.LettersStreamService;
import com.eoren.lettersstream.service.WordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Slf4j
public class LettersStreamController {

  private final LettersStreamService lettersStreamService;
  private final WordService wordService;

  @PostMapping(value = "/letters", consumes = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<LetterResponse> receiveLetters(@RequestBody String letter) {
    if (wordService.getWord() == null) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(LetterResponse
          .builder()
          .matching(false)
          .reason(WORD_MISSING_ERROR)
          .build());
    }
    if (letter == null) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(LetterResponse
          .builder()
          .matching(false)
          .reason(LETTERS_MISSING_ERROR)
          .build());
    }
    if (letter.trim().length() != 1) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(LetterResponse
          .builder()
          .matching(false)
          .reason(ONE_CHARACTER_ONLY_ERROR)
          .build());
    }
    boolean subSequence = lettersStreamService.isSubSequence(letter);
    return ResponseEntity.ok(LetterResponse.builder().matching(subSequence).build());
  }

  @GetMapping(value = "/word/{word}")
  public ResponseEntity<String> defineWord(@PathVariable("word") String word) {
    if (word == null || word.isEmpty()) {
      return ResponseEntity
          .badRequest()
          .body(WORD_MISSING_ERROR);
    }
    wordService.setWord(word);
    return ResponseEntity.ok(String.format("Word '%s' has been set successfully", word));
  }
}
