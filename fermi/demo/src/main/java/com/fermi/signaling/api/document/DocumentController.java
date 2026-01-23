package com.fermi.signaling.api.document;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

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
}
