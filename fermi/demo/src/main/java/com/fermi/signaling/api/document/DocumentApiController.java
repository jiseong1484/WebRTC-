package com.fermi.signaling.api.document;

import com.fermi.signaling.api.document.dto.FinalizeDocumentRequest;
import com.fermi.signaling.application.document.DocumentStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sessions/{sessionId}/documents")
public class DocumentApiController {

    private final DocumentStorageService documentStorageService;

    public DocumentApiController(DocumentStorageService documentStorageService) {
        this.documentStorageService = documentStorageService;
    }

    @PostMapping
    public ResponseEntity<Void> finalizeDocument(
            @PathVariable String sessionId,
            @RequestBody FinalizeDocumentRequest request
    ) {
        documentStorageService.generateAndSaveFinalPdf(sessionId, request.documentName(), request.fieldValues());
        return ResponseEntity.ok().build();
    }
}
