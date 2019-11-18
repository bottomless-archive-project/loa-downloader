package com.github.loa.vault.client.service;

import com.github.loa.compression.domain.DocumentCompression;
import com.github.loa.document.service.domain.DocumentEntity;
import com.github.loa.vault.client.configuration.VaultClientConfigurationProperties;
import com.github.loa.vault.client.service.domain.ArchivingContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static java.net.http.HttpRequest.BodyPublishers.ofString;

@Slf4j
@Service
@RequiredArgsConstructor
public class VaultClientService {

    private final VaultClientConfigurationProperties vaultClientConfigurationProperties;
    private final WebClient vaultWebClient;

    public Mono<Void> archiveDocument(final ArchivingContext documentStageLocation) {
        return vaultWebClient.post()
                .uri(URI.create("http://" + vaultClientConfigurationProperties.getHost() + ":"
                        + vaultClientConfigurationProperties.getPort() + "/document"))
                .body(BodyInserters.fromMultipartData("contents", new FileSystemResource(
                        documentStageLocation.getContents())).with("document", documentStageLocation)
                )
                .retrieve()
                .bodyToMono(Void.class);
    }

    public Mono<byte[]> queryDocument(final DocumentEntity documentEntity) {
        return vaultWebClient.get()
                .uri("/document/" + documentEntity.getId())
                .retrieve()
                .bodyToMono(byte[].class)
                .timeout(Duration.ofMinutes(5));
    }

    public void recompressDocument(final DocumentEntity documentEntity, final DocumentCompression documentCompression) {
        final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://" + vaultClientConfigurationProperties.getHost() + ":"
                        + vaultClientConfigurationProperties.getPort() + "/document/" + documentEntity.getId()
                        + "/recompress"))
                .header("Content-Type", "application/json")
                .POST(ofString("{compression: \"" + documentCompression + "\"}"))
                .build();

        HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .join();
    }
}
