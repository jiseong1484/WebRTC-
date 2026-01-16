package com.fermi.signaling.api.session.dto;

import java.time.Instant;

public record CreateSessionResponse(
        String sessionId,
        String agentUrl,
        String customerUrl,
        Instant expiresAt
) {}