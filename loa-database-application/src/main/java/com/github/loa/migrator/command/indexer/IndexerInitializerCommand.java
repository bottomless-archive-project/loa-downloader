package com.github.loa.migrator.command.indexer;

import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.ingest.PutPipelineRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty("initialize-indexer")
public class IndexerInitializerCommand implements CommandLineRunner {

    private final RestHighLevelClient restHighLevelClient;

    @Override
    public void run(final String... args) throws IOException {
        initializeAttachmentPipeline();
    }

    private void initializeAttachmentPipeline() throws IOException {
        final String source = "{\"description\":\"Processors for the Library of Alexandria project.\", " +
                "\"processors\":[{\"attachment\":{\"field\":\"content\", \"indexed_chars\" : -1}}]}";

        final PutPipelineRequest request = new PutPipelineRequest("vault-document-pipeline",
                new BytesArray(source.getBytes(StandardCharsets.UTF_8)), XContentType.JSON
        );

        restHighLevelClient.ingest().putPipeline(request, RequestOptions.DEFAULT);
    }
}