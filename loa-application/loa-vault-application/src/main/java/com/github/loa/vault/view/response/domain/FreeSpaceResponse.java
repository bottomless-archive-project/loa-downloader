package com.github.loa.vault.view.response.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@Value
@Builder
public class FreeSpaceResponse {

    long freeSpace;
}
