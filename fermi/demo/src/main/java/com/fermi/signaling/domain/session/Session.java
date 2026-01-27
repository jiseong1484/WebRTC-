package com.fermi.signaling.domain.session;

import com.fermi.signaling.domain.document.CompletedDocument;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sessions")
@Getter
@Setter
@NoArgsConstructor
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String sessionId;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant expiresAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SessionStatus status;

    @Column(nullable = false)
    private String agentId;

    @Column(nullable = false)
    private String customerId;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompletedDocument> completedDocuments = new ArrayList<>();

    public Session(String sessionId, String agentId, String customerId, Instant createdAt, Instant expiresAt) {
        this.sessionId = sessionId;
        this.agentId = agentId;
        this.customerId = customerId;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.status = SessionStatus.MATCHED;
    }

    public boolean isExpired(Instant now) {
        return now.isAfter(expiresAt);
    }

    public void expireIfNeeded(Instant now) {
        if (status == SessionStatus.ACTIVE && isExpired(now)) {
            status = SessionStatus.EXPIRED;
        }
    }

    public void end() {
        this.status = SessionStatus.ENDED;
    }
}