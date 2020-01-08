package com.github.loa.vault.configuration;

import com.github.loa.queue.artemis.service.domain.ClientConsumerRegistryBean;
import com.github.loa.queue.service.domain.Queue;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.artemis.api.core.ActiveMQException;
import org.apache.activemq.artemis.api.core.client.ClientConsumer;
import org.apache.activemq.artemis.api.core.client.ClientSession;
import org.apache.activemq.artemis.api.core.client.ClientSessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class VaultQueueConfiguration {

    @Bean(destroyMethod = "stop")
    public ClientSession clientSession(final ClientSessionFactory clientSessionFactory) throws ActiveMQException {
        return clientSessionFactory.createSession();
    }

    @Bean
    public ClientConsumerRegistryBean clientConsumer(final ClientSession clientSession) throws ActiveMQException {
        final ClientConsumer clientConsumer = clientSession.createConsumer(Queue.DOCUMENT_ARCHIVING_QUEUE.getAddress());

        try {
            clientSession.start();
        } catch (final ActiveMQException e) {
            log.error("Connection error with the Queue Application!", e);
        }

        return ClientConsumerRegistryBean.builder()
                .queue(Queue.DOCUMENT_ARCHIVING_QUEUE)
                .clientConsumer(clientConsumer)
                .build();
    }
}