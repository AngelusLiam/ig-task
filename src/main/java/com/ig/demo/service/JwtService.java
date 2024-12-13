package com.ig.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${ig.provider.token-url}")
    private String TOKEN_URL;

    @Value("${ig.provider.client-id}")
    private String CLIENT_ID;

    @Value("${ig.provider.client-secret}")
    private String CLIENT_SECRET;

    @Value("${ig.provider.grant-type}")
    private String GRANT_TYPE;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String fetchToken(String username, String password) {
        WebClient webClient = WebClient.create();

        // Crea un corpo con i parametri form-url-encoded
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", GRANT_TYPE);
        formData.add("client_id", CLIENT_ID);
        formData.add("client_secret", CLIENT_SECRET);
        formData.add("username", username);
        formData.add("password", password);

        String tokenResponse = webClient.post()
                .uri(UriComponentsBuilder.fromHttpUrl(TOKEN_URL).toUriString())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .bodyValue(formData)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return parseToken(tokenResponse);
    }

    private String parseToken(String response) {
        try {
            Map<String, Object> responseMap = objectMapper.readValue(response, Map.class);
            return responseMap.get("access_token").toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse token response", e);
        }
    }
}
