Perfect 👍 Thanks for sharing your project zip. Let’s do this step by step:

We’ll integrate two things into your Spring Boot backend:

1. File Upload + Reading (with Apache Tika for text extraction)


2. Automatic Compliance Check (using OpenAI API)




---

🛠 Steps to Add File Reading + Compliance Check

1. Add Dependencies in pom.xml

Add these:

<dependencies>
    <!-- Apache Tika -->
    <dependency>
        <groupId>org.apache.tika</groupId>
        <artifactId>tika-core</artifactId>
        <version>2.9.2</version>
    </dependency>
    <dependency>
        <groupId>org.apache.tika</groupId>
        <artifactId>tika-parsers-standard-package</artifactId>
        <version>2.9.2</version>
    </dependency>

    <!-- OpenAI API (via official client) -->
    <dependency>
        <groupId>com.theokanning.openai-gpt3-java</groupId>
        <artifactId>client</artifactId>
        <version>0.18.2</version>
    </dependency>
</dependencies>


---

2. Create DTOs

src/main/java/com/policyvault/dto/PolicyRequestDto.java

package com.policyvault.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PolicyRequestDto {
    private MultipartFile file;
}

src/main/java/com/policyvault/dto/ComplianceResponseDto.java

package com.policyvault.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ComplianceResponseDto {
    private boolean compliant;
    private String summary;
}


---

3. File Storage Utility

src/main/java/com/policyvault/util/FileStorageUtil.java

package com.policyvault.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class FileStorageUtil {

    private static final String UPLOAD_DIR = "uploads/";

    public String saveFile(MultipartFile file) throws IOException {
        Files.createDirectories(Paths.get(UPLOAD_DIR));
        String filePath = UPLOAD_DIR + file.getOriginalFilename();
        file.transferTo(new File(filePath));
        return filePath;
    }
}


---

4. Text Extraction Service

src/main/java/com/policyvault/service/DocumentParserService.java

package com.policyvault.service;

import org.apache.tika.Tika;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class DocumentParserService {

    private final Tika tika = new Tika();

    public String extractText(File file) throws Exception {
        return tika.parseToString(file);
    }
}


---

5. Compliance Checker Service (OpenAI API)

src/main/java/com/policyvault/service/ComplianceCheckerService.java

package com.policyvault.service;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class ComplianceCheckerService {

    private final OpenAiService openAiService;

    public ComplianceCheckerService(@Value("${openai.api.key}") String apiKey) {
        this.openAiService = new OpenAiService(apiKey, Duration.ofSeconds(30));
    }

    public String checkCompliance(String documentText) {
        String prompt = "You are a compliance officer. Check if the following policy text " +
                "complies with Indian Labour Laws. Respond with 'Compliant' or 'Non-Compliant' " +
                "and give a short explanation:\n\n" + documentText;

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-4o-mini") // cheaper + fast
                .messages(List.of(new ChatMessage("user", prompt)))
                .maxTokens(300)
                .build();

        return openAiService.createChatCompletion(request)
                .getChoices()
                .get(0)
                .getMessage()
                .getContent();
    }
}


---

6. Controller

src/main/java/com/policyvault/controller/PolicyController.java

package com.policyvault.controller;

import com.policyvault.dto.ComplianceResponseDto;
import com.policyvault.dto.PolicyRequestDto;
import com.policyvault.service.ComplianceCheckerService;
import com.policyvault.service.DocumentParserService;
import com.policyvault.util.FileStorageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/api/policies")
@RequiredArgsConstructor
public class PolicyController {

    private final FileStorageUtil fileStorageUtil;
    private final DocumentParserService documentParserService;
    private final ComplianceCheckerService complianceCheckerService;

    @PostMapping("/upload")
    public ComplianceResponseDto uploadPolicy(@RequestParam("file") MultipartFile file) {
        try {
            // Save file locally
            String filePath = fileStorageUtil.saveFile(file);

            // Extract text
            String text = documentParserService.extractText(new File(filePath));

            // Send to OpenAI for compliance check
            String result = complianceCheckerService.checkCompliance(text);

            boolean compliant = result.toLowerCase().contains("compliant") && !result.toLowerCase().contains("non-compliant");
            return new ComplianceResponseDto(compliant, result);

        } catch (Exception e) {
            return new ComplianceResponseDto(false, "Error processing file: " + e.getMessage());
        }
    }
}


---

7. Application Properties

src/main/resources/application.properties

server.port=8080
spring.servlet.multipart.max-file-size=20MB
spring.servlet.multipart.max-request-size=20MB

openai.api.key=YOUR_OPENAI_API_KEY


---

✅ With this setup:

Upload PDF → Stored locally (/uploads)

Extract text with Tika

Send text to OpenAI for compliance analysis

Return compliance result



---

Would you like me to also wire in a PostgreSQL entity + repository so every uploaded policy (file metadata + compliance result) is saved in DB for later search/filter?

# PolicyVault
