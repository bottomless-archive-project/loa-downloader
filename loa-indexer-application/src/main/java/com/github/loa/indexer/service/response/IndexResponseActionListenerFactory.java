package com.github.loa.indexer.service.response;

import com.github.loa.document.service.DocumentManipulator;
import com.github.loa.indexer.service.response.domain.IndexResponseActionListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IndexResponseActionListenerFactory {

    private final DocumentManipulator documentManipulator;

    public IndexResponseActionListener newListener() {
        return new IndexResponseActionListener(documentManipulator);
    }
}
