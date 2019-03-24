package com.github.loa.downloader.download.service.document;

import com.github.loa.document.service.DocumentIdFactory;
import com.github.loa.document.service.DocumentManipulator;
import com.github.loa.document.service.entity.factory.DocumentEntityFactory;
import com.github.loa.downloader.download.service.file.DocumentFileManipulator;
import com.github.loa.downloader.download.service.file.DocumentFileValidator;
import com.github.loa.downloader.download.service.file.FileDownloader;
import com.github.loa.downloader.download.service.file.domain.FileDownloaderException;
import com.github.loa.downloader.download.service.file.domain.FileManipulatingException;
import com.github.loa.stage.service.StageLocationFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

/**
 * This service is responsible for downloading documents.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentDownloader {

    private final DocumentEntityFactory documentEntityFactory;
    private final FileDownloader fileDownloader;
    private final DocumentIdFactory documentIdFactory;
    private final StageLocationFactory stageLocationFactory;
    private final DocumentFileValidator documentFileValidator;
    private final DocumentDownloadEvaluator documentDownloadEvaluator;
    private final DocumentManipulator documentManipulator;
    private final DocumentFileManipulator documentFileManipulator;

    public void downloadDocument(final URL documentLocation) {
        log.debug("Starting to download document {}.", documentLocation);

        if (!documentDownloadEvaluator.evaluateDocumentLocation(documentLocation)) {
            return;
        }

        final String documentId = documentIdFactory.newDocumentId(documentLocation);
        final File stageFileLocation = stageLocationFactory.newLocation(documentId);

        try {
            fileDownloader.downloadFile(documentLocation, stageFileLocation, 30000);
        } catch (FileDownloaderException e) {
            log.info("Failed to download document!", e);

            documentManipulator.markFailed(documentId);

            return;
        }

        final String crc = calculateHash(stageFileLocation);
        final long fileSize = stageFileLocation.length();

        // The ordering like this is for a reason! We don't want to set the file size and crc values of invalid files!
        if (!documentFileValidator.isValidDocument(documentId)) {
            documentFileManipulator.cleanup(documentId);
            documentManipulator.markInvalid(documentId);

            return;
        }

        // Validate if the file already exists or not. Set the file size and crc afterwards, even if the file is a
        // duplicate. We can't set it previously because then it will be always found as a "duplicate".
        if (documentEntityFactory.isDocumentExists(crc, fileSize)) {
            documentFileManipulator.cleanup(documentId);
            documentManipulator.markDuplicate(documentId, fileSize, crc);

            return;
        }

        try {
            documentFileManipulator.moveToVault(documentId);
        } catch (FileManipulatingException e) {
            log.error("Failed while processing the downloaded document.", e);

            documentManipulator.markProcessFailure(documentId);

            return;
        }

        documentManipulator.markDownloaded(documentId, fileSize, crc);
    }

    //TODO: Move this to it's own class/service
    private String calculateHash(final File documentDownloadingLocation) {
        try {
            try (final BufferedInputStream documentInputStream =
                         new BufferedInputStream(new FileInputStream(documentDownloadingLocation))) {
                return DigestUtils.sha256Hex(documentInputStream);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to calculate file hash!", e);
        }
    }
}
