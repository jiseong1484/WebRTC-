package com.fermi.signaling.infrastructure.session;

import com.fermi.signaling.domain.session.Session;

import java.util.Optional;

public interface SessionRepository {
    Session save(Session session);
    Optional<Session> findById(String sessionId);
}