package org.example.qdrant;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import io.metaloom.qdrant.client.http.QDrantHttpClient;
import io.metaloom.qdrant.client.http.impl.HttpErrorException;
import io.metaloom.qdrant.client.http.model.collection.CollectionCreateRequest;
import io.metaloom.qdrant.client.http.model.collection.config.Distance;
import io.metaloom.qdrant.client.http.model.point.NamedVector;
import io.metaloom.qdrant.client.http.model.point.Payload;
import io.metaloom.qdrant.client.http.model.point.PointStruct;
import io.metaloom.qdrant.client.http.model.point.PointsListUpsertRequest;
import io.metaloom.qdrant.client.http.model.point.PointsSearchRequest;
import io.metaloom.qdrant.client.http.model.point.ScoredPoint;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class QdrantService {

  public static final String EMBEDDINGS_VECTOR = "embeddings";
  private final QDrantHttpClient client;

  public QdrantService(String host, int port) {
    this.client = QDrantHttpClient.builder().setHostname(host).setPort(port).build();
  }

  public void createCollection(String collectionName, int dimension) throws HttpErrorException {
    var req = new CollectionCreateRequest();
    req.setVectors(EMBEDDINGS_VECTOR, dimension, Distance.COSINE);
    client.createCollection(collectionName, req).sync();
  }

  public void insertPoint(int id, String collectionName, List<Double> embedding, String sentence)
      throws HttpErrorException {
    log.info("Inserting point with id {} into collection {}", id, collectionName);
    var request = new PointsListUpsertRequest();
    var floats = convertToFloats(embedding);
    var json = JsonNodeFactory.instance.objectNode().put("sentence", sentence);
    var payload = new Payload().setJson(json);
    request.setPoints(PointStruct.of(EMBEDDINGS_VECTOR, floats).setPayload(payload).setId(id));
    var response = client.upsertPoints(collectionName, request, false).sync();
    log.info("insertPoint response: {}", response);
  }

  private float[] convertToFloats(List<Double> embedding) {
    float[] floats = new float[embedding.size()];
    for (int i = 0; i < embedding.size(); i++) {
      floats[i] = embedding.get(i).floatValue();
    }
    return floats;
  }

  public List<ScoredPoint> search(List<Double> searchEmbedding, String collection)
      throws HttpErrorException {
    var request = new PointsSearchRequest();
    request.setVector(NamedVector.of(EMBEDDINGS_VECTOR, convertToFloats(searchEmbedding)));
    request.setLimit(100);
    request.setWithPayload(true);

    return client.searchPoints(collection, request).sync().getResult();
  }
}
