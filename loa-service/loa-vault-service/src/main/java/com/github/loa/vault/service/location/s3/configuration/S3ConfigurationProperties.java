package com.github.loa.vault.service.location.s3.configuration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConstructorBinding
@RequiredArgsConstructor
@ConfigurationProperties("loa.vault.location.s3")
public class S3ConfigurationProperties {

    private final String host;
    private final String port;
    private final String accessKey;
    private final String secretKey;
    private final String region;
    private final String bucketName;
}
