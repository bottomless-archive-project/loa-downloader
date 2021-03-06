package com.github.loa.vault.service.transformer;

import com.github.loa.document.service.domain.DocumentType;
import com.github.loa.queue.service.domain.message.DocumentArchivingMessage;
import com.github.loa.vault.configuration.VaultConfigurationProperties;
import com.github.loa.vault.service.domain.DocumentArchivingContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentArchivingContextTransformer {

    private final VaultConfigurationProperties vaultConfigurationProperties;

    public DocumentArchivingContext transform(final DocumentArchivingMessage documentArchivingMessage) {
        return DocumentArchivingContext.builder()
                .id(UUID.fromString(documentArchivingMessage.getId()))
                .vault(vaultConfigurationProperties.getName())
                .type(DocumentType.valueOf(documentArchivingMessage.getType()))
                .source(documentArchivingMessage.getSource())
                .contentLength(documentArchivingMessage.getContentLength())
                .content(documentArchivingMessage.getContent())
                .versionNumber(vaultConfigurationProperties.getVersionNumber())
                .build();
    }
}
