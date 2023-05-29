package org.example.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.List;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.ToString;

@Data
public class TestData {
  @JsonProperty("test_cases")
  private List<TestCase> testCases;

  @Data
  @ToString
  public static class TestCase {
    private String english;
    private String polish;

    @JsonProperty("modified_polish")
    private String modifiedPolish;

    @JsonProperty("additional_english")
    private String additionalEnglish;
  }

  @SneakyThrows
  public static TestData loadTestData() {
    ObjectMapper objectMapper = new ObjectMapper();
    InputStream inputStream = TestData.class.getResourceAsStream("/test-cases.json");
    return objectMapper.readValue(inputStream, TestData.class);
  }
}
