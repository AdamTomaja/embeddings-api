package org.example;

import io.metaloom.qdrant.client.http.model.point.ScoredPoint;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.example.data.TestData;

@Slf4j
public class CompareApp extends AbstractTestApp {

  private static final String TEST_CASE_NAME = "embeddings-tests-01H1KMFM58BE1WBY3S6BD5JTX1";

  @Override
  protected String getTestCaseName() {
    return TEST_CASE_NAME;
  }

  public static void main(String[] args) throws Exception {
    CompareApp compareApp = new CompareApp();
    compareApp.compare();
  }

  private void compare() throws Exception {
    var testCases = TestData.loadTestData().getTestCases();
    runFor(testCases, openAiConnector, getOpenAiCollection());
//    runFor(testCases, embeddingsApiService, getEmbeddingsApiCollection());
  }

  private void runFor(
      List<TestData.TestCase> testCases, EmbeddingsGenerator generator, String collection)
      throws Exception {
    System.out.println("%s: ".formatted(generator.getClass().getSimpleName()));
    System.out.println(
        "sentence\tdirect_polish\tdirect_english\tpolish_modified\tenglish_modified\tpolish_to_english\tenglish_to_polish");
    for (int i = 0; i < testCases.size(); i++) {
      var testCase = testCases.get(i);
      System.out.println(
          "%s\t%s\t%s\t%s\t%s\t%s\t%s"
              .formatted(
                  testCase.getPolish(),
                  compare(testCase.getPolish(), testCase.getPolish(), generator, collection),
                  compare(testCase.getEnglish(), testCase.getEnglish(), generator, collection),
                  compare(
                      testCase.getPolish(), testCase.getModifiedPolish(), generator, collection),
                  compare(
                      testCase.getEnglish(),
                      testCase.getAdditionalEnglish(),
                      generator,
                      collection),
                  compare(testCase.getPolish(), testCase.getEnglish(), generator, collection),
                  compare(testCase.getEnglish(), testCase.getPolish(), generator, collection)));
    }
  }

  private Float compare(
      String search, String expected, EmbeddingsGenerator embeddingsGenerator, String collection)
      throws Exception {
    List<Double> searchEmbedding = embeddingsGenerator.getEmbeddings(search);
    var results = qdrantService.search(searchEmbedding, collection);
    return results.stream()
        .filter(p -> p.getPayload().getJson().get("sentence").asText().equals(expected))
        .map(ScoredPoint::getScore)
        .findFirst()
        .orElse(0F);
  }
}
