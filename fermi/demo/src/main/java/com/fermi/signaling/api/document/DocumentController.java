package com.fermi.signaling.api.document;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fermi.signaling.api.document.dto.DocumentResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final ObjectMapper objectMapper;

    public DocumentController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public ResponseEntity<List<String>> getDocumentList() {
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            // Look for all files in the static/documents directory
            Resource[] resources = resolver.getResources("classpath*:static/documents/*.pdf");

            List<String> filenames = Arrays.stream(resources)
                    .map(Resource::getFilename)
                    .filter(filename -> filename != null && !filename.isEmpty())
                    .collect(Collectors.toList());

            return ResponseEntity.ok(filenames);
        } catch (IOException e) {
            // Log the exception in a real application
            e.printStackTrace();
            return ResponseEntity.status(500).body(Collections.emptyList());
        }
    }

    @GetMapping("/{documentName}")
    public ResponseEntity<DocumentResponse> getDocumentDetails(@PathVariable String documentName) {
        if (documentName == null || documentName.contains("..")) {
            return ResponseEntity.badRequest().build();
        }

        try {
            String pdfPath = "static/documents/" + documentName;
            String jsonPath = "static/documents/" + documentName.replace(".pdf", ".json");

            Resource jsonResource = new ClassPathResource(jsonPath);
            List<Object> fields = Collections.emptyList();

            if (jsonResource.exists()) {
                try (InputStream inputStream = jsonResource.getInputStream()) {
                    fields = objectMapper.readValue(inputStream, new TypeReference<List<Object>>() {});
                }
            }

            // Note: We are sending the URL, not the file content.
            // The frontend will still fetch the PDF from the static path.
            // This is a stepping stone. A more secure implementation would
            // serve the PDF via a controller that checks authorization.
            String pdfUrl = "/documents/" + documentName;

            DocumentResponse response = new DocumentResponse(pdfUrl, fields);
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}
