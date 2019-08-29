package br.com.samuel.sambatech.services;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MainService {

  private final String KEY_RESULT_BIT_MOVIN = "result";
  private final String KEY_DATA_BIT_MOVIN = "data";

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private ObjectMapper mapper;

  @Value("${bit.movin.url}")
  private String urlBaseBitMovin;

  @Value("${bit.movin.api.key}")
  private String apiKey;

  protected <T> ResponseEntity<String> httpMethod(String urlApi, T object, HttpMethod method) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("X-Api-Key", apiKey);
    HttpEntity<?> request = new HttpEntity<>(getJSON(object), headers);
    return restTemplate.exchange(urlBaseBitMovin + urlApi, method, request, String.class);
  }

  private <T> String getJSON(T instance) throws RuntimeException {
    try {
      return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(instance);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("unchecked")
  public <T> T returnObject(String endpoint, T instance, HttpMethod method)
      throws RuntimeException {
    try {
      ResponseEntity<String> jsonResult = httpMethod(endpoint, instance, method);
      JSONObject resultObject = getDataObject(jsonResult.getBody())
          .getJSONObject(KEY_DATA_BIT_MOVIN).getJSONObject(KEY_RESULT_BIT_MOVIN);
      if (resultObject != null) {
        return (T) getParser(resultObject.toString(), instance.getClass());
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return null;
  }

  private JSONObject getDataObject(String json) {
    return new JSONObject(json).getJSONObject(KEY_DATA_BIT_MOVIN);
  }

  private <T> T getParser(String json, Class<T> cls) {
    try {
      return mapper.readValue(getDataObject(json).toString(), cls);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
