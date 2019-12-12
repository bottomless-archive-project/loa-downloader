package com.github.loa.vault.client.configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class VaultClientConfiguration {

    private static final int VAULT_CLIENT_TIMEOUT = 120000;

    private final VaultClientConfigurationProperties vaultClientConfigurationProperties;

    @Bean
    public WebClient vaultWebClient(
            @Qualifier("vaultClientHttpConnector") final ClientHttpConnector vaultClientHttpConnector) {
        return WebClient.builder()
                .baseUrl("http://" + vaultClientConfigurationProperties.getHost() + ":"
                        + vaultClientConfigurationProperties.getPort())
                .clientConnector(vaultClientHttpConnector)
                .build();
    }

    @Bean
    protected ClientHttpConnector vaultClientHttpConnector(
            @Qualifier("vaultHttpClient") final HttpClient vaultHttpClient) {
        return new ReactorClientHttpConnector(vaultHttpClient);
    }

    @Bean
    protected HttpClient vaultHttpClient(
            @Qualifier("vaultTcpClient") final TcpClient vaultTcpClient) {
        return HttpClient.from(vaultTcpClient)
                .compress(true);
    }

    @Bean
    protected TcpClient vaultTcpClient() {
        return TcpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, VAULT_CLIENT_TIMEOUT)
                .doOnConnected(connection -> connection
                        .addHandlerLast(new ReadTimeoutHandler(VAULT_CLIENT_TIMEOUT, TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(VAULT_CLIENT_TIMEOUT, TimeUnit.MILLISECONDS))
                );
    }

    @Bean
    public CircuitBreaker vaultCircuitBreaker() {
        return CircuitBreaker.ofDefaults("vault-circuit-breaker");
    }
}
