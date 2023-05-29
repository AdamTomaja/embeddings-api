package org.example.openai;

import static com.theokanning.openai.service.OpenAiService.defaultClient;
import static com.theokanning.openai.service.OpenAiService.defaultObjectMapper;
import static com.theokanning.openai.service.OpenAiService.defaultRetrofit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.OpenAiApi;
import com.theokanning.openai.embedding.EmbeddingRequest;
import com.theokanning.openai.service.OpenAiService;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import okhttp3.OkHttpClient;
import org.example.EmbeddingsGenerator;
import retrofit2.Retrofit;

public class OpenAiConnector implements EmbeddingsGenerator {

  private final OpenAiService openAiService;

  public OpenAiConnector() {
    ObjectMapper objectMapper = defaultObjectMapper();
    OkHttpClient client = defaultClient(System.getenv("OPENAI_API_KEY"), Duration.ofSeconds(30));
    Retrofit retrofit = defaultRetrofit(client, objectMapper);
    OpenAiApi api = retrofit.create(OpenAiApi.class);
    openAiService = new OpenAiService(api);
  }

  public List<Double> getEmbeddings(String sentence) {
    var request =
        EmbeddingRequest.builder()
            .model("text-embedding-ada-002")
            .input(Collections.singletonList(sentence))
            .build();
    var response = openAiService.createEmbeddings(request);
    return response.getData().get(0).getEmbedding();
  }
}
