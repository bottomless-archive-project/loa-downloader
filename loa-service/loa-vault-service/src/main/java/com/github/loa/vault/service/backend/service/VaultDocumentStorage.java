package com.github.loa.vault.service.backend.service;

import com.github.loa.compression.service.provider.CompressionServiceProvider;
import com.github.loa.document.service.domain.DocumentEntity;
import com.github.loa.vault.domain.exception.VaultAccessException;
import com.github.loa.vault.service.location.VaultLocationFactory;
import com.github.loa.vault.service.backend.domain.VaultPersistenceException;
import com.github.loa.vault.service.location.VaultLocation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * The document storage is responsible to store and later retrieve the content of a document.
 */
@Service
@RequiredArgsConstructor
public class VaultDocumentStorage {

    private final VaultLocationFactory vaultLocationFactory;
    private final CompressionServiceProvider compressionServiceProvider;

    /**
     * Store the document contents.
     *
     * @param documentEntity   the document to store the contents for
     * @param documentContents the contents of the document
     */
    public void persistDocument(final DocumentEntity documentEntity, final InputStream documentContents) {
        try (final VaultLocation vaultLocation = vaultLocationFactory.getLocation(documentEntity)) {
            persistDocument(documentEntity, documentContents, vaultLocation);
        } catch (final IOException e) {
            throw new VaultAccessException("Unable to move document with id " + documentEntity.getId()
                    + " to the vault!", e);
        }
    }

    /**
     * Store the document contents on the provided {@link VaultLocation}.
     *
     * @param documentEntity   the document to store the contents for
     * @param documentContents the contents of the document
     * @param vaultLocation    the vault location where the document's contents should be stored
     */
    public void persistDocument(final DocumentEntity documentEntity, final InputStream documentContents,
            final VaultLocation vaultLocation) {
        if (!documentEntity.isCompressed()) {
            try (final OutputStream outputStream = vaultLocation.destination()) {
                IOUtils.copy(documentContents, outputStream);
            } catch (final IOException e) {
                throw new VaultPersistenceException("Unable to move document with id " + documentEntity.getId()
                        + " to the vault!", e);
            }
        } else {
            try (final OutputStream outputStream = compressionServiceProvider
                    .getCompressionService(documentEntity.getCompression()).compress(vaultLocation.destination())) {
                IOUtils.copy(documentContents, outputStream);
            } catch (final IOException e) {
                throw new VaultPersistenceException("Unable to move document with id " + documentEntity.getId()
                        + " to the vault!", e);
            }
        }
    }
}
