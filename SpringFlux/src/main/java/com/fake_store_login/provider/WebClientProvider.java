package com.fake_store_login.provider;

import com.fake_store_login.exception.WebClientException;
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


    /**
     * Ejecuta un request HTTP con body (POST, PUT, PATCH)
     *
     * @param clientName            Nombre del cliente (para logs)
     * @param method                Método HTTP
     * @param uri                   URI completa
     * @param timeout               Timeout en milisegundos
     * @param body                  Body de la request
     * @param headers               Headers (puede ser null)
     * @param responseTypeReference Clase del response
     * @return Response convertido al tipo especificado
     * //
     */

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
                // .body(Mono.just(body), body.getClass())
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


    /**
     * Ejecuta un request HTTP sin body (GET, DELETE)
     *
     * @param clientName            Nombre del cliente (para logs)
     * @param method                Método HTTP
     * @param uri                   URI completa
     * @param timeout               Timeout en milisegundos
     * @param headers               Headers (puede ser null)
     * @param responseTypeReference Clase del response
     * @return Response convertido al tipo especificado
     */
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