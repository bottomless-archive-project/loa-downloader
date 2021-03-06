package com.github.loa.vault.service.location.file.configuration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

/**
 * A {@link ConfigurationProperties} class that contains the configuration of the vault location if the vault type
 * property is {@link com.github.loa.vault.domain.VaultType#FILE} at runtime.
 */
@Getter
@ConstructorBinding
@RequiredArgsConstructor
@ConfigurationProperties("loa.vault.location.file")
public class FileConfigurationProperties {

    /**
     * The path to the vault on the local filesystem.
     */
    private final String path;
}
