package org.godigit.policyvault.service.impl;

import org.apache.tika.Tika;
import org.godigit.policyvault.service.DocumentParserService;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class DocumentParserServiceImpl implements DocumentParserService {

    private final Tika tika = new Tika();

    @Override
    public String extractText(File file) throws Exception {
        return tika.parseToString(file);
    }
}