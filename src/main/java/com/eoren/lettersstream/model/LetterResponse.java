package com.eoren.lettersstream.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LetterResponse {

  private boolean matching;
  private String reason;
}
