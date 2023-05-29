package org.example.embeddingsapi;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class EmbeddingsApiRequest {

  private List<String> sentences;
}
