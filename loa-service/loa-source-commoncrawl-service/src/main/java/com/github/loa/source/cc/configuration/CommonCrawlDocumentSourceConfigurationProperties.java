package com.github.loa.source.cc.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("loa.source.commoncrawl")
public class CommonCrawlDocumentSourceConfigurationProperties {

    /**
     * The Id of the crawl. For example CC-MAIN-2019-09.
     */
    private String crawlId;

    /**
     * The Id of the WARC file in the crawl. Usually between 1 - 64000.
     */
    private int warcId;
}