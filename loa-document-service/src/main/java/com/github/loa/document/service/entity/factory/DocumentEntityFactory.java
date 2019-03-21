package com.github.loa.document.service.entity.factory;

import com.github.loa.document.repository.DocumentRepository;
import com.github.loa.document.service.domain.DocumentEntity;
import com.github.loa.document.service.DocumentEntityTransformer;
import com.github.loa.document.service.entity.factory.domain.DocumentCreationContext;
import com.github.loa.document.repository.domain.DocumentDatabaseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentEntityFactory {

    private final DocumentRepository documentRepository;
    private final DocumentEntityTransformer documentEntityTransformer;

    /**
     * Check if a document exists based on the provided document id.
     *
     * @param documentId the id of the document to check
     * @return return true if a document exists with the provided id
     */
    public boolean isDocumentExists(final String documentId) {
        return documentRepository.findById(documentId) != null;
    }

    /**
     * Return true if any document exists with the provided crc and file size.
     *
     * @param crc      the crc to use for the checking
     * @param fileSize the file size used for the checking
     * @return return true if a document exist with the provided parameters or false otherwise
     */
    public boolean isDocumentExists(final String crc, final long fileSize) {
        return !documentRepository.findByCrcAndFileSize(crc, fileSize).isEmpty();
    }

    /**
     * Return the document entities belonging to the provided crc and file size values.
     *
     * @param crc      the crc value of the document
     * @param fileSize the file size value of the document
     * @return the list of documents with the provided values
     */
    public List<DocumentEntity> getDocumentEntity(final String crc, final long fileSize) {
        final List<DocumentDatabaseEntity> tomeDatabaseEntity = documentRepository.findByCrcAndFileSize(crc, fileSize);

        return tomeDatabaseEntity.stream()
                .map(documentEntityTransformer::transform)
                .collect(Collectors.toList());
    }

    /**
     * Creates a new document. The document is persisted to the database.
     *
     * @param documentCreationContext the parameters of the document to create
     */
    public void newDocumentEntity(final DocumentCreationContext documentCreationContext) {
        final DocumentDatabaseEntity documentDatabaseEntity = new DocumentDatabaseEntity();

        documentDatabaseEntity.setId(documentCreationContext.getId());
        documentDatabaseEntity.setUrl(documentCreationContext.getLocation().toString());
        documentDatabaseEntity.setStatus(documentCreationContext.getStatus().toString());
        documentDatabaseEntity.setCrc(documentCreationContext.getCrc());
        documentDatabaseEntity.setFileSize(documentCreationContext.getFileSize());
        documentDatabaseEntity.setDownloaderVersion(documentCreationContext.getVersionNumber());
        documentDatabaseEntity.setSource(documentCreationContext.getSource());

        documentRepository.insertDocument(documentDatabaseEntity);
    }
}