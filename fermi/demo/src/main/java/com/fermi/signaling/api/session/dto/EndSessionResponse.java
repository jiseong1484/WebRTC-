package com.fermi.signaling.api.session.dto;

import com.fermi.signaling.domain.session.SessionStatus;

import java.time.Instant;

public record EndSessionResponse(
        String sessionId,
        SessionStatus status,
        Instant expiresAt
) {}