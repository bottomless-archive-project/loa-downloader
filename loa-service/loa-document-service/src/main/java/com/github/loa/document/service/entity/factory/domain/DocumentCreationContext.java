package com.github.loa.document.service.entity.factory.domain;

import com.github.loa.compression.domain.DocumentCompression;
import com.github.loa.document.service.domain.DocumentStatus;
import com.github.loa.document.service.domain.DocumentType;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class DocumentCreationContext {

    private final UUID id;
    private final String vault;
    private final DocumentType type;
    private final String source;
    private final String checksum;
    private final long fileSize;
    private final DocumentStatus status;
    private final DocumentCompression compression;
    private final int versionNumber;
}
