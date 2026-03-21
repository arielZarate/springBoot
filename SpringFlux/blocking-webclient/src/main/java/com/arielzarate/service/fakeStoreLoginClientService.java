package com.arielzarate.service;

import com.arielzarate.dto.LoginRequest;
import com.arielzarate.dto.LoginResponse;
import com.arielzarate.exception.WebClientException;
import com.arielzarate.provider.WebClientMethod;
import com.arielzarate.provider.WebClientProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Map;

@Slf4j
@Service
public class fakeStoreLoginClientService {

    private final WebClientProvider webClientProvider;

    @Value("${fake-store.base-url}")
    private String baseUrl;

    @Value("${fake-store.timeout}")
    private long timeout;

    @Value("${fake-store.credentials.username}")
    private String username;

    @Value("${fake-store.credentials.password}")
    private String password;

    public fakeStoreLoginClientService(WebClientProvider webClientProvider) {
        this.webClientProvider = webClientProvider;
    }

    public String login() {
        log.info("Attempting login for user: {}", username);

        String uri = baseUrl + "/auth/login";
        LoginRequest request = new LoginRequest();
        request.setUsername(username);
        request.setPassword(password);

        Map<String, String> headers = Map.of("Content-Type", "application/json");

        try {
            LoginResponse response = webClientProvider.applyWithBody(
                    "FakeStoreAPI Login Client",
                    WebClientMethod.POST,
                    URI.create(uri),
                    request,
                    timeout,
                    headers,
                    LoginResponse.class
            );

            log.info("Login successful for user: {}", username);
            return response.getToken();

        } catch (WebClientException e) {
            log.error("Login failed for user: {} - Error: {}", username, e.getMessage());
            throw e;
        }
    }
}
