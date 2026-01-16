package com.fermi.signaling.api.session.dto;

import com.fermi.signaling.domain.session.SessionStatus;

import java.time.Instant;

public record GetSessionResponse(
        String sessionId,
        SessionStatus status,
        Instant createdAt,
        Instant expiresAt
) {}