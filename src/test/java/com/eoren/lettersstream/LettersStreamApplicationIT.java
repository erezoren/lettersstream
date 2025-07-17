package com.eoren.lettersstream;

import static org.assertj.core.api.Assertions.assertThat;

import com.eoren.lettersstream.model.LetterResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class LettersStreamApplicationIT {

  @LocalServerPort
  private int port;
  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  void defineWord_and_receiveLetters_happyFlow() {
    String word = "abc";
    ResponseEntity<String> wordResponse = restTemplate.getForEntity("/v1/word/" + word, String.class);
    assertThat(wordResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(wordResponse.getBody()).contains("Word 'abc' has been set successfully");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.TEXT_PLAIN);
    HttpEntity<String> request = new HttpEntity<>("a", headers);

    ResponseEntity<LetterResponse> letterResponse = restTemplate.postForEntity("/v1/letters", request, LetterResponse.class);
    assertThat(letterResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(letterResponse.getBody()).isNotNull();
    assertThat(letterResponse.getBody().isMatching()).isFalse();

    request = new HttpEntity<>("b", headers);

    letterResponse = restTemplate.postForEntity("/v1/letters", request, LetterResponse.class);
    assertThat(letterResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(letterResponse.getBody()).isNotNull();
    assertThat(letterResponse.getBody().isMatching()).isFalse();

    request = new HttpEntity<>("c", headers);

    letterResponse = restTemplate.postForEntity("/v1/letters", request, LetterResponse.class);
    assertThat(letterResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(letterResponse.getBody()).isNotNull();
    assertThat(letterResponse.getBody().isMatching()).isTrue();
  }

  @Test
  void receiveLetters_shouldReturnErrorIfNoWordSet() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.TEXT_PLAIN);
    HttpEntity<String> request = new HttpEntity<>("a", headers);

    ResponseEntity<LetterResponse> response = restTemplate.postForEntity("/v1/letters", request, LetterResponse.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().isMatching()).isFalse();
    assertThat(response.getBody().getReason()).isNotEmpty();
  }
}
