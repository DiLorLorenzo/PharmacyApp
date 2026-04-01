package lorenzodl.PharmacyApp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PayPalService {

    @Value("${paypal.client.id}")
    private String clientId;

    @Value("${paypal.client.secret}")
    private String clientSecret;

    @Value("${paypal.base-url}")
    private String baseUrl;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    public String createOrder(Double total) {
        try {
            String accessToken = getAccessToken();

            String url = baseUrl + "/v2/checkout/orders";

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> amount = new HashMap<>();
            amount.put("currency_code", "EUR");
            amount.put("value", String.format(java.util.Locale.US, "%.2f", total));

            Map<String, Object> purchaseUnit = new HashMap<>();
            purchaseUnit.put("amount", amount);

            Map<String, Object> applicationContext = new HashMap<>();
            applicationContext.put("return_url", "http://localhost:5173/success");
            applicationContext.put("cancel_url", "http://localhost:5173/cancel");

            Map<String, Object> body = new HashMap<>();
            body.put("intent", "CAPTURE");
            body.put("purchase_units", List.of(purchaseUnit));
            body.put("application_context", applicationContext);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            JsonNode json = objectMapper.readTree(response.getBody());
            return json.get("id").asText();

        } catch (Exception e) {
            throw new RuntimeException("Errore durante la creazione dell'ordine PayPal: " + e.getMessage());
        }
    }

    public String captureOrder(String orderId) {
        try {
            String accessToken = getAccessToken();

            String url = baseUrl + "/v2/checkout/orders/" + orderId + "/capture";

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>("{}", headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            JsonNode json = objectMapper.readTree(response.getBody());
            return json.get("status").asText();

        } catch (Exception e) {
            throw new RuntimeException("Errore durante il capture dell'ordine PayPal: " + e.getMessage());
        }
    }

    private String getAccessToken() {
        try {
            String url = baseUrl + "/v1/oauth2/token";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            String auth = clientId + ":" + clientSecret;
            String encodedAuth = Base64.getEncoder()
                    .encodeToString(auth.getBytes(StandardCharsets.UTF_8));
            headers.set("Authorization", "Basic " + encodedAuth);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "client_credentials");

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            JsonNode json = objectMapper.readTree(response.getBody());
            return json.get("access_token").asText();

        } catch (Exception e) {
            throw new RuntimeException("Errore durante il recupero dell'access token PayPal: " + e.getMessage());
        }
    }
}