package com.arielzarate.provider;

import com.arielzarate.exception.WebClientException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Slf4j
@AllArgsConstructor
@Component
public class WebClientProvider {

    private final WebClient webClient;

    public <T, R> R applyWithBody(
            String clientName,
            WebClientMethod method,
            URI uri,
            T body,
            long timeout,
            Map<String, String> headers,
            Class<R> responseTypeReference
    ) {
        log.info("Calling {} with method {} to uri {}", clientName, method.name(), uri);

        return webClient
                .method(HttpMethod.valueOf(method.name()))
                .uri(uri)
                .headers(h -> {
                    if (headers != null) {
                        h.setAll(headers);
                    }
                })
                .bodyValue(body)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> {
                    log.error("Calling in {} response with ERROR {} {}", clientName, method.name(), uri);
                    return Mono.error(new WebClientException("Error calling client" + clientName + response.statusCode()));
                })
                .bodyToMono(responseTypeReference)
                .timeout(Duration.ofMillis(timeout))
                .onErrorResume(TimeoutException.class, ex -> {
                    log.error("Call TIMEOUT {} {} - {}", method.name(), uri, ex.getMessage());
                    return Mono.error(new WebClientException("Error calling " + clientName + " " + ex));
                })
                .block();
    }

    public <R> R applyWithoutBody(
            String clientName,
            WebClientMethod method,
            URI uri,
            long timeout,
            Map<String, String> headers,
            Class<R> responseTypeReference) {

        log.info("Calling {} {} {}", clientName, method.name(), uri);

        return webClient
                .method(HttpMethod.valueOf(method.name()))
                .uri(uri)
                .headers(header -> {
                    if (headers != null) {
                        header.setAll(headers);
                    }
                })
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> {
                    log.error("Call to {} response ERROR {} {}", clientName, method.name(), uri);
                    return Mono.error(new WebClientException("Error calling " + clientName + ": " + response.statusCode()));
                })
                .bodyToMono(responseTypeReference)
                .timeout(Duration.ofMillis(timeout))
                .onErrorResume(TimeoutException.class, ex -> {
                    log.error("Call TIMEOUT {} {} - {}", method.name(), uri, ex.getMessage());
                    return Mono.error(new WebClientException("Timeout calling " + clientName, ex));
                })
                .block();
    }
}
