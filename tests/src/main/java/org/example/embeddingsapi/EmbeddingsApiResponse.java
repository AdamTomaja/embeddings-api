package org.example.embeddingsapi;

import java.util.List;
import lombok.Data;

@Data
public class EmbeddingsApiResponse {
  private List<List<Double>> embeddings;
}
