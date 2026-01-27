package com.fermi.signaling.application.document;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fermi.signaling.application.session.SessionService;
import com.fermi.signaling.domain.document.CompletedDocument;
import com.fermi.signaling.domain.document.CompletedDocumentRepository;
import com.fermi.signaling.domain.session.Session;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class DocumentStorageService {

    private final SessionService sessionService;
    private final CompletedDocumentRepository completedDocumentRepository;
    private final ObjectMapper objectMapper;
    private final Path rootLocation;

    // A simple representation of the JSON field structure
    private record FieldDefinition(String fieldId, String type, int page, Rect rect) {}
    private record Rect(float x, float y, float width, float height) {}

    public DocumentStorageService(SessionService sessionService, CompletedDocumentRepository completedDocumentRepository, ObjectMapper objectMapper) {
        this.sessionService = sessionService;
        this.completedDocumentRepository = completedDocumentRepository;
        this.objectMapper = objectMapper;
        this.rootLocation = Paths.get("completed-documents");
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    @Transactional
    public void generateAndSaveFinalPdf(String sessionId, String documentName, Map<String, Object> fieldValues) {
        try {
            Session session = sessionService.getSessionOrThrow(sessionId);

            // 1. Load original PDF and its JSON layout file
            ClassPathResource originalPdfResource = new ClassPathResource("static/documents/" + documentName);
            String jsonPath = "static/documents/" + documentName.replace(".pdf", ".json");
            ClassPathResource jsonResource = new ClassPathResource(jsonPath);

            List<FieldDefinition> fieldDefinitions;
            try (InputStream jsonInputStream = jsonResource.getInputStream()) {
                fieldDefinitions = objectMapper.readValue(jsonInputStream, new TypeReference<>() {});
            }

            try (PDDocument pdDocument = Loader.loadPDF(originalPdfResource.getInputStream().readAllBytes())) {

                // 2. Iterate through submitted values and draw them on the PDF
                for (Map.Entry<String, Object> entry : fieldValues.entrySet()) {
                    String fieldId = entry.getKey();
                    Object fieldValue = entry.getValue();

                    // Find the definition for this field
                    fieldDefinitions.stream()
                        .filter(def -> def.fieldId().equals(fieldId))
                        .findFirst()
                        .ifPresent(def -> {
                            try {
                                // PDF pages are 0-indexed, JSON might be 1-indexed or more. Assume it's not 0-indexed.
                                // The provided JSON is 3, so we need to access page 2.
                                PDPage page = pdDocument.getPage(def.page() - 1);
                                
                                // PDFBox Y-coordinate starts from the bottom, UI/JSON usually from the top.
                                float pageHeight = page.getMediaBox().getHeight();
                                float transformedY = pageHeight - def.rect().y() - def.rect().height();

                                try (PDPageContentStream contentStream = new PDPageContentStream(pdDocument, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
                                    switch (def.type()) {
                                        case "keyboard":
                                            if (fieldValue instanceof String && !((String) fieldValue).isEmpty()) {
                                                contentStream.beginText();
                                                // Adjust font size and position for better alignment
                                                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
                                                contentStream.newLineAtOffset(def.rect().x() + 2, transformedY + (def.rect().height() / 4));
                                                contentStream.showText((String) fieldValue);
                                                contentStream.endText();
                                            }
                                            break;
                                        case "checkbox":
                                            if (fieldValue instanceof Boolean && (Boolean) fieldValue) {
                                                contentStream.beginText();
                                                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.ZAPF_DINGBATS), 12);
                                                contentStream.newLineAtOffset(def.rect().x(), transformedY + (def.rect().height() / 4) + 2);
                                                contentStream.showText("✔"); // Checkmark character
                                                contentStream.endText();
                                            }
                                            break;
                                        case "signature":
                                            if (fieldValue instanceof String) {
                                                String valueStr = (String) fieldValue;
                                                if (valueStr.startsWith("data:image/png;base64,")) {
                                                    byte[] imageBytes = Base64.getDecoder().decode(valueStr.substring("data:image/png;base64,".length()));
                                                    PDImageXObject pdImage = PDImageXObject.createFromByteArray(pdDocument, imageBytes, "signature");
                                                    contentStream.drawImage(pdImage, def.rect().x(), transformedY, def.rect().width(), def.rect().height());
                                                }
                                            }
                                            break;
                                    }
                                }
                            } catch (IOException e) {
                                // Wrap in a runtime exception to propagate up to the main catch block
                                throw new RuntimeException("Failed to draw field " + fieldId, e);
                            }
                        });
                }

                // 3. Save the modified document to a new file
                String newFileName = UUID.randomUUID() + ".pdf";
                Path destinationFile = this.rootLocation.resolve(Paths.get(newFileName)).normalize().toAbsolutePath();
                pdDocument.save(new File(destinationFile.toString()));

                // 4. Create and save the database record
                CompletedDocument completedDocument = new CompletedDocument(session, documentName, destinationFile.toString());
                completedDocumentRepository.save(completedDocument);

            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate or save final PDF for session " + sessionId, e);
        }
    }
}
