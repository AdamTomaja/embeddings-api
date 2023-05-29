package org.example;

import com.github.f4b6a3.ulid.Ulid;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.example.data.TestData;

@Slf4j
public class GenerateDataApp extends AbstractTestApp {

  private String testCaseName;
  private AtomicInteger idCounterOpenaAi = new AtomicInteger(0);
  private AtomicInteger idCounterEmbeddingsApi = new AtomicInteger(0);

  public void generateData() throws Exception {
    testCaseName = "embeddings-tests-" + Ulid.fast();

    qdrantService.createCollection(getEmbeddingsApiCollection(), 768);
    qdrantService.createCollection(getOpenAiCollection(), 1536);

    var testCases = TestData.loadTestData().getTestCases();

    for (int i = 0; i < testCases.size(); i++) {
      var testCase = testCases.get(i);
      generateTestCaseEmbeddings(testCase);
    }

    log.info("Generated data with prefix: {}", testCaseName);
  }

  private void generateTestCaseEmbeddings(TestData.TestCase testCase) throws Exception {
    log.info("Generating embeddins for test case {}", testCase);
    generateEmbedding(
        idCounterEmbeddingsApi.incrementAndGet(),
        getEmbeddingsApiCollection(),
        embeddingsApiService,
        testCase.getPolish());
    generateEmbedding(
        idCounterEmbeddingsApi.incrementAndGet(),
        getEmbeddingsApiCollection(),
        embeddingsApiService,
        testCase.getModifiedPolish());
    generateEmbedding(
        idCounterEmbeddingsApi.incrementAndGet(),
        getEmbeddingsApiCollection(),
        embeddingsApiService,
        testCase.getEnglish());
    generateEmbedding(
        idCounterEmbeddingsApi.incrementAndGet(),
        getEmbeddingsApiCollection(),
        embeddingsApiService,
        testCase.getAdditionalEnglish());

    generateEmbedding(
        idCounterOpenaAi.incrementAndGet(),
        getOpenAiCollection(),
        openAiConnector,
        testCase.getPolish());
    generateEmbedding(
        idCounterOpenaAi.incrementAndGet(),
        getOpenAiCollection(),
        openAiConnector,
        testCase.getModifiedPolish());
    generateEmbedding(
        idCounterOpenaAi.incrementAndGet(),
        getOpenAiCollection(),
        openAiConnector,
        testCase.getEnglish());
    generateEmbedding(
        idCounterOpenaAi.incrementAndGet(),
        getOpenAiCollection(),
        openAiConnector,
        testCase.getAdditionalEnglish());
  }

  private void generateEmbedding(
      int id, String collectionName, EmbeddingsGenerator generator, String sentence)
      throws Exception {
    List<Double> embeddings = generator.getEmbeddings(sentence);
    qdrantService.insertPoint(id, collectionName, embeddings, sentence);
  }

  public static void main(String[] args) throws Exception {
    var app = new GenerateDataApp();
    app.generateData();
  }

  @Override
  protected String getTestCaseName() {
    return testCaseName;
  }
}
