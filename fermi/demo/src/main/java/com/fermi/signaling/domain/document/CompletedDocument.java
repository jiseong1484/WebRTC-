package com.fermi.signaling.domain.document;

import com.fermi.signaling.domain.session.Session;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class CompletedDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    @Column(nullable = false)
    private String originalDocumentName;

    @Column(nullable = false)
    private String savedDocumentPath;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected CompletedDocument() {}

    public CompletedDocument(Session session, String originalDocumentName, String savedDocumentPath) {
        this.session = session;
        this.originalDocumentName = originalDocumentName;
        this.savedDocumentPath = savedDocumentPath;
        this.createdAt = LocalDateTime.now();
    }

    // Getters
    public Long getId() { return id; }
    public Session getSession() { return session; }
    public String getOriginalDocumentName() { return originalDocumentName; }
    public String getSavedDocumentPath() { return savedDocumentPath; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
