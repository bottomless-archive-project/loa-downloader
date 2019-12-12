package com.github.loa.downloader.service.file;

import com.github.loa.document.service.domain.DocumentType;
import com.github.loa.downloader.configuration.DownloaderConfigurationProperties;
import com.github.loa.parser.service.DocumentDataParser;
import com.github.loa.stage.service.StageLocationFactory;
import com.github.loa.stage.service.domain.StageLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.File;
import java.nio.file.Path;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

//TODO: Re-enable this!
@Disabled
class DocumentFileValidatorTest {

    private final static String DOCUMENT_ID = "123";
    private final static DocumentType DOCUMENT_TYPE = DocumentType.DOC;

    private final DownloaderConfigurationProperties downloaderConfigurationProperties =
            new DownloaderConfigurationProperties();

    private DocumentDataParser documentDataParser;
    private StageLocationFactory stageLocationFactory;
    private DocumentFileValidator underTest;

    @BeforeEach
    private void setup() {
        documentDataParser = mock(DocumentDataParser.class);
        stageLocationFactory = mock(StageLocationFactory.class);

        underTest = new DocumentFileValidator(documentDataParser, stageLocationFactory, downloaderConfigurationProperties);
    }

    @Test
    void testIsValidDocumentWhenDocumentIsTooSmall() {
        final Path documentFile = mock(Path.class);

        when(stageLocationFactory.getLocation(DOCUMENT_ID, DOCUMENT_TYPE))
                .thenReturn(Mono.just(
                        StageLocation.builder()
                                .path(documentFile)
                                .build()
                ));

        final Mono<Boolean> result = underTest.isValidDocument(DOCUMENT_ID, DOCUMENT_TYPE);

        StepVerifier.create(result)
                .consumeNextWith(Assertions::assertFalse)
                .verifyComplete();
    }

    @Test
    void testIsValidDocumentWhenDocumentIsTooBig() {
        final Path documentFile = mock(Path.class);

        when(stageLocationFactory.getLocation(DOCUMENT_ID, DOCUMENT_TYPE))
                .thenReturn(Mono.just(
                        StageLocation.builder()
                                .path(documentFile)
                                .build()
                ));

        final Mono<Boolean> result = underTest.isValidDocument(DOCUMENT_ID, DOCUMENT_TYPE);

        StepVerifier.create(result)
                .consumeNextWith(Assertions::assertFalse)
                .verifyComplete();
    }

    @Test
    void testIsValidDocumentWhenDocumentIsGood() {
        final Path documentFile = mock(Path.class);

        when(stageLocationFactory.getLocation(DOCUMENT_ID, DOCUMENT_TYPE))
                .thenReturn(Mono.just(
                        StageLocation.builder()
                                .path(documentFile)
                                .build()
                ));

        final Mono<Boolean> result = underTest.isValidDocument(DOCUMENT_ID, DOCUMENT_TYPE);

        StepVerifier.create(result)
                .consumeNextWith(Assertions::assertTrue)
                .verifyComplete();
    }
}