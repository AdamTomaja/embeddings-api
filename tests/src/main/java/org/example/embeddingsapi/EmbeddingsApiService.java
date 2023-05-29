package org.example.embeddingsapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.example.EmbeddingsGenerator;

@Slf4j
public class EmbeddingsApiService implements EmbeddingsGenerator {
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final String host;
  private final int port;

  public EmbeddingsApiService(String host, int port) {
    this.host = host;
    this.port = port;
  }

  public EmbeddingsApiResponse getEmbeddings(EmbeddingsApiRequest embeddingRequest)
      throws IOException, InterruptedException {
    var httpClient = HttpClient.newHttpClient();
    var request =
        HttpRequest.newBuilder()
            .uri(URI.create("http://" + host + ":" + port + "/embeddings"))
            .POST(
                HttpRequest.BodyPublishers.ofString(
                    objectMapper.writeValueAsString(embeddingRequest)))
            .header("Content-Type", "application/json")
            .build();
    var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    return objectMapper.readValue(response.body(), EmbeddingsApiResponse.class);
  }

  @Override
  public List<Double> getEmbeddings(String sentence) throws Exception {
    return getEmbeddings(new EmbeddingsApiRequest(Collections.singletonList(sentence)))
        .getEmbeddings()
        .get(0);
  }
}
