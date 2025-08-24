package org.godigit.policyvault.service;

import java.io.File;

public interface DocumentParserService {
    String extractText(File file) throws Exception;
}
