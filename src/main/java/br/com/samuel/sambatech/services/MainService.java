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
import br.com.samuel.sambatech.dto.error.ErrorResponseDTO;

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
      JSONObject jSONObject = new JSONObject(jsonResult.getBody());
      JSONObject resultObject;
      resultObject =
          jSONObject.getJSONObject(KEY_DATA_BIT_MOVIN).getJSONObject(KEY_RESULT_BIT_MOVIN);
      if (resultObject != null) {
        return (T) mapper.readValue(resultObject.toString(), instance.getClass());
      }
      ErrorResponseDTO error = mapper.readValue(
          jSONObject.getJSONObject(KEY_DATA_BIT_MOVIN).toString(), ErrorResponseDTO.class);
      throw new RuntimeException(error.toString());
    } catch (Exception e) {
      throw new RuntimeException(e.toString());
    }
  }
}
