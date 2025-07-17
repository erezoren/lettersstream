package com.eoren.lettersstream.reasource;

import static com.eoren.lettersstream.model.Errors.ONE_CHARACTER_ONLY_ERROR;
import static com.eoren.lettersstream.model.Errors.WORD_MISSING_ERROR;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.eoren.lettersstream.service.LettersStreamService;
import com.eoren.lettersstream.service.WordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(LettersStreamController.class)
public class LettersStreamControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private LettersStreamService lettersStreamService;
  @MockBean
  private WordService wordService;

  @Test
  void receiveLetters_shouldReturnMatchingTrue() throws Exception {
    given(wordService.getWord()).willReturn("abc");
    given(lettersStreamService.isSubSequence("a")).willReturn(true);

    mockMvc.perform(post("/v1/letters")
            .contentType(MediaType.TEXT_PLAIN)
            .content("a"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.matching").value(true));

    verify(lettersStreamService).isSubSequence("a");
  }

  @Test
  void receiveLetters_shouldReturnErrorIfNoWordSet() throws Exception {
    given(wordService.getWord()).willReturn(null);

    mockMvc.perform(post("/v1/letters")
            .contentType(MediaType.TEXT_PLAIN)
            .content("a"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.matching").value(false))
        .andExpect(jsonPath("$.reason").value(WORD_MISSING_ERROR));
  }

  @Test
  void receiveLetters_shouldReturnErrorIfInputInvalid() throws Exception {
    given(wordService.getWord()).willReturn("abc");

    mockMvc.perform(post("/v1/letters")
            .contentType(MediaType.TEXT_PLAIN)
            .content("ab"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.matching").value(false))
        .andExpect(jsonPath("$.reason").value(ONE_CHARACTER_ONLY_ERROR));
  }

  @Test
  void defineWord_shouldSetWordAndReturnSuccess() throws Exception {
    mockMvc.perform(get("/v1/word/hello"))
        .andExpect(status().isOk())
        .andExpect(content().string("Word 'hello' has been set successfully"));

    verify(wordService).setWord("hello");
  }

  @Test
  void defineWord_shouldReturnErrorIfWordMissing() throws Exception {
    mockMvc.perform(get("/v1/word/"))
        .andExpect(status().isNotFound());
  }
}