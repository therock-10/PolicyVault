AIzaSyBBUXjHrh14Yu1ekTir2g60EKGfA4td5hk
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

Nice — below I’ll give you a drop-in, backend-ready update that adds PostgreSQL persistence for uploaded PDFs + compliance findings and integrates that storage into the system you already have.

I’ll:

1. Show the new/updated classes (entity, repository, mapper, service changes, controller change).


2. Show the application.properties you need to run with Postgres.


3. Explain the quick steps to wire it and demo with cURL.



I’ll keep the mapper simple (manual @Component) so you don’t need MapStruct setup for the 2-day MVP.


---

1) New entity — PolicyCheck.java

package com.policyvault.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "policy_check")
public class PolicyCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String policyName;
    private String description;
    private String filePath;

    @Lob
    @Column(columnDefinition = "text")
    private String policyText;

    private boolean compliant;

    @Column(columnDefinition = "text")
    private String reason;

    private OffsetDateTime uploadedAt = OffsetDateTime.now();

    // getters & setters (or use Lombok @Data)
    // ----
    public Long getId(){return id;}
    public void setId(Long id){this.id = id;}
    public String getPolicyName(){return policyName;}
    public void setPolicyName(String policyName){this.policyName = policyName;}
    public String getDescription(){return description;}
    public void setDescription(String description){this.description = description;}
    public String getFilePath(){return filePath;}
    public void setFilePath(String filePath){this.filePath = filePath;}
    public String getPolicyText(){return policyText;}
    public void setPolicyText(String policyText){this.policyText = policyText;}
    public boolean isCompliant(){return compliant;}
    public void setCompliant(boolean compliant){this.compliant = compliant;}
    public String getReason(){return reason;}
    public void setReason(String reason){this.reason = reason;}
    public OffsetDateTime getUploadedAt(){return uploadedAt;}
    public void setUploadedAt(OffsetDateTime uploadedAt){this.uploadedAt = uploadedAt;}
}


---

2) Repository — PolicyCheckRepository.java

package com.policyvault.repository;

import com.policyvault.entity.PolicyCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PolicyCheckRepository extends JpaRepository<PolicyCheck, Long> {
    // add custom queries here if needed later
}


---

3) Request DTO & Response DTO (small update)

PolicyUploadRequestDto.java

package com.policyvault.dto;

public class PolicyUploadRequestDto {
    private String policyName;
    private String description;
    private String policyText; // populated after extraction

    // getters/setters
    public String getPolicyName(){return policyName;}
    public void setPolicyName(String policyName){this.policyName = policyName;}
    public String getDescription(){return description;}
    public void setDescription(String description){this.description = description;}
    public String getPolicyText(){return policyText;}
    public void setPolicyText(String policyText){this.policyText = policyText;}
}

ComplianceResponseDto.java

package com.policyvault.dto;

import java.time.OffsetDateTime;

public class ComplianceResponseDto {
    private Long id;
    private boolean compliant;
    private String reason;
    private String filePath;
    private OffsetDateTime uploadedAt;

    // getters/setters
    public Long getId(){return id;}
    public void setId(Long id){this.id = id;}
    public boolean isCompliant(){return compliant;}
    public void setCompliant(boolean compliant){this.compliant = compliant;}
    public String getReason(){return reason;}
    public void setReason(String reason){this.reason = reason;}
    public String getFilePath(){return filePath;}
    public void setFilePath(String filePath){this.filePath = filePath;}
    public OffsetDateTime getUploadedAt(){return uploadedAt;}
    public void setUploadedAt(OffsetDateTime uploadedAt){this.uploadedAt = uploadedAt;}
}


---

4) Simple Mapper Component — PolicyMapper.java

package com.policyvault.mapper;

import com.policyvault.dto.ComplianceResponseDto;
import com.policyvault.dto.PolicyUploadRequestDto;
import com.policyvault.entity.PolicyCheck;
import org.springframework.stereotype.Component;

@Component
public class PolicyMapper {

    public PolicyCheck toEntity(PolicyUploadRequestDto dto) {
        PolicyCheck e = new PolicyCheck();
        e.setPolicyName(dto.getPolicyName());
        e.setDescription(dto.getDescription());
        e.setPolicyText(dto.getPolicyText());
        return e;
    }

    public ComplianceResponseDto toDto(PolicyCheck e) {
        ComplianceResponseDto dto = new ComplianceResponseDto();
        dto.setId(e.getId());
        dto.setCompliant(e.isCompliant());
        dto.setReason(e.getReason());
        dto.setFilePath(e.getFilePath());
        dto.setUploadedAt(e.getUploadedAt());
        return dto;
    }
}


---

5) Update ComplianceCheckerService to return a structured result

Replace the current method that returns a raw String with a small result class and parsing:

LlmResult.java

package com.policyvault.service;

public class LlmResult {
    private boolean compliant;
    private String reason;
    private String rawResponse;

    public LlmResult(boolean compliant, String reason, String rawResponse) {
        this.compliant = compliant;
        this.reason = reason;
        this.rawResponse = rawResponse;
    }

    public boolean isCompliant(){return compliant;}
    public String getReason(){return reason;}
    public String getRawResponse(){return rawResponse;}
}

Modify existing ComplianceCheckerService:

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
    private final String model;

    public ComplianceCheckerService(@Value("${openai.api.key}") String apiKey,
                                    @Value("${app.llm-model:gpt-4o-mini}") String model) {
        this.openAiService = new OpenAiService(apiKey, Duration.ofSeconds(60));
        this.model = model;
    }

    public LlmResult checkCompliance(String documentText) {
        String prompt = """
            You are a precise Indian labour-law compliance checker.
            For the policy text given, output a short judgement with "Compliant" or "Non-Compliant"
            followed by a brief explanation. Return plain text.
            Then finish.
            Policy Text:
            """ + documentText;

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(model)
                .messages(List.of(new ChatMessage("user", prompt)))
                .maxTokens(500)
                .temperature(0.0)
                .build();

        String response = openAiService.createChatCompletion(request)
                                       .getChoices().get(0).getMessage().getContent();

        // crude parse: determine compliant vs non-compliant by keyword presence.
        boolean compliant = response.toLowerCase().contains("compliant") &&
                            !response.toLowerCase().contains("non-compliant");
        // If both occur, prefer "non-compliant" if explicitly present.
        if (response.toLowerCase().contains("non-compliant")) compliant = false;

        String reason = response.trim();
        return new LlmResult(compliant, reason, response);
    }
}

> Note: this simple parsing is fine for the MVP — later you can prompt the model to return strict JSON and parse it.




---

6) New ComplianceService — persisting the check

ComplianceService.java

package com.policyvault.service;

import com.policyvault.dto.PolicyUploadRequestDto;
import com.policyvault.entity.PolicyCheck;
import com.policyvault.mapper.PolicyMapper;
import com.policyvault.repository.PolicyCheckRepository;
import com.policyvault.util.FileStorageUtil;
import com.policyvault.service.DocumentParserService;
import com.policyvault.dto.ComplianceResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
public class ComplianceService {

    private final FileStorageUtil fileStorageUtil;
    private final DocumentParserService documentParserService;
    private final ComplianceCheckerService complianceCheckerService;
    private final PolicyCheckRepository policyRepository;
    private final PolicyMapper policyMapper;

    public ComplianceService(FileStorageUtil fileStorageUtil,
                             DocumentParserService documentParserService,
                             ComplianceCheckerService complianceCheckerService,
                             PolicyCheckRepository policyRepository,
                             PolicyMapper policyMapper) {
        this.fileStorageUtil = fileStorageUtil;
        this.documentParserService = documentParserService;
        this.complianceCheckerService = complianceCheckerService;
        this.policyRepository = policyRepository;
        this.policyMapper = policyMapper;
    }

    @Transactional
    public ComplianceResponseDto handleUploadAndCheck(MultipartFile file, String policyName, String description) throws Exception {
        // 1) store file locally
        String filePath = fileStorageUtil.saveFile(file);

        // 2) extract text
        String text = documentParserService.extractText(new File(filePath));

        // 3) build DTO -> entity
        PolicyUploadRequestDto dto = new PolicyUploadRequestDto();
        dto.setPolicyName(policyName != null ? policyName : file.getOriginalFilename());
        dto.setDescription(description);
        dto.setPolicyText(text);

        PolicyCheck entity = policyMapper.toEntity(dto);
        entity.setFilePath(filePath);

        // 4) call LLM
        LlmResult result = complianceCheckerService.checkCompliance(text);

        // 5) persist results
        entity.setCompliant(result.isCompliant());
        entity.setReason(result.getReason());
        policyRepository.save(entity);

        // 6) return response DTO
        return policyMapper.toDto(entity);
    }
}


---

7) Update Controller — PolicyController.java

package com.policyvault.controller;

import com.policyvault.dto.ComplianceResponseDto;
import com.policyvault.service.ComplianceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/policies")
@RequiredArgsConstructor
public class PolicyController {

    private final ComplianceService complianceService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ComplianceResponseDto uploadPolicy(
            @RequestPart("file") MultipartFile file,
            @RequestParam(value = "policyName", required = false) String policyName,
            @RequestParam(value = "description", required = false) String description
    ) throws Exception {
        return complianceService.handleUploadAndCheck(file, policyName, description);
    }

    // Add endpoints to fetch persisted records later (optional)
}


---

8) FileStorageUtil & DocumentParserService

You already had these; ensure they exist and are the versions from the earlier integration. (No change needed besides making sure the upload path is writable by your app.)


---

9) Add/verify Maven dependencies

Make sure pom.xml contains these (add if missing):

<!-- Spring Data JPA -->
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- PostgreSQL driver -->
<dependency>
  <groupId>org.postgresql</groupId>
  <artifactId>postgresql</artifactId>
  <scope>runtime</scope>
</dependency>

<!-- Apache Tika (if not already) -->
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

<!-- OpenAI client -->
<dependency>
  <groupId>com.theokanning.openai-gpt3-java</groupId>
  <artifactId>client</artifactId>
  <version>0.18.2</version>
</dependency>

(You may already have some of these — check pom.xml and add missing ones.)


---

10) application.properties (example)

Replace values with your DB credentials. For dev you can use ddl-auto=update to create schema automatically.

server.port=8080

# Postgres
spring.datasource.url=jdbc:postgresql://localhost:5432/policydb
spring.datasource.username=policy
spring.datasource.password=policy
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true
spring.jpa.show-sql=false

# upload limits
spring.servlet.multipart.max-file-size=20MB
spring.servlet.multipart.max-request-size=20MB

# OpenAI key (recommended to pass as env and reference here)
openai.api.key=${OPENAI_API_KEY}
app.llm-model=gpt-4o-mini

> For production, do NOT keep secrets in application.properties — use environment variables or secrets manager.




---

11) How to run & test (quick)

1. Start Postgres and create DB/user:

CREATE DATABASE policydb;
CREATE USER policy WITH ENCRYPTED PASSWORD 'policy';
GRANT ALL PRIVILEGES ON DATABASE policydb TO policy;


2. Set your OpenAI key in env:

export OPENAI_API_KEY="sk_..."


3. Build and run:

mvn clean package
java -jar target/policy-compliance-system-0.0.1-SNAPSHOT.jar


4. Upload a PDF and check:

curl -X POST "http://localhost:8080/api/policies/upload" \
  -H "Content-Type: multipart/form-data" \
  -F "file=@/path/to/HR-Policy.pdf" \
  -F "policyName=HR Policy 2025" \
  -F "description=Demo upload" | jq

Response will contain the persisted id, compliant flag, reason, filePath, and uploadedAt.




---

12) Notes & next improvements

Strict JSON output from LLM: for more reliable parsing, change the prompt to ask the LLM to return JSON like:

{"compliant": true, "reason": "..."}

and parse it with Jackson. I kept a permissive parser for speed of demo.

Chunking long docs: huge PDFs may exceed token limits. To be robust, split long documents into clauses/paragraphs and call LLM per chunk; aggregate results and store them (you can store per-clause findings in a policy_clause + compliance_finding table later).

Audit log: save the LLM raw response if you want to review or re-run checks later. You can add a raw_response JSONB column to policy_check.

Error handling & retries: add retries/backoff for LLM calls in case of transient failures.

Security: sanitize file names, validate uploads, and set proper filesystem permissions.



---

If you want, I can:

produce the exact Java files (complete with imports) you can drop into your project, or

modify the existing zip you uploaded and return updated files (I can prepare diff/patch content you can apply).


Which do you prefer?

