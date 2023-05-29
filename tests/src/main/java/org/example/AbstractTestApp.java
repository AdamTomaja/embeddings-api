package org.example;

import org.example.embeddingsapi.EmbeddingsApiService;
import org.example.openai.OpenAiConnector;
import org.example.qdrant.QdrantService;

public abstract class AbstractTestApp {
  protected final QdrantService qdrantService;
  protected final EmbeddingsApiService embeddingsApiService;
  protected final OpenAiConnector openAiConnector;

  public AbstractTestApp() {
    embeddingsApiService = new EmbeddingsApiService("vault", 5000);
    qdrantService = new QdrantService("vault", 6333);
    openAiConnector = new OpenAiConnector();
  }

  protected String getOpenAiCollection() {
    return getTestCaseName() + "-openai";
  }

  protected String getEmbeddingsApiCollection() {
    return getTestCaseName() + "-embeddings-api";
  }

  protected abstract String getTestCaseName();
}
