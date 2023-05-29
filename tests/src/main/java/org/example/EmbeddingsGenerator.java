package org.example;

import java.util.List;

public interface EmbeddingsGenerator {
  List<Double> getEmbeddings(String sentence) throws Exception;
}
