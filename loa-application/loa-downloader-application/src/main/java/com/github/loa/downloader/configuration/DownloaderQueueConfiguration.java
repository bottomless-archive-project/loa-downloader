package com.github.loa.downloader.configuration;

import org.apache.activemq.artemis.api.core.ActiveMQException;
import org.apache.activemq.artemis.api.core.client.ClientConsumer;
import org.apache.activemq.artemis.api.core.client.ClientSession;
import org.apache.activemq.artemis.api.core.client.ClientSessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DownloaderQueueConfiguration {

    @Bean
    public ClientConsumer clientConsumer(final ClientSession clientSession) throws ActiveMQException {
        return clientSession.createConsumer("loa-document-location");
    }

    @Bean
    public ClientSession clientSession(final ClientSessionFactory clientSessionFactory) throws ActiveMQException {
        return clientSessionFactory.createSession();
    }
}