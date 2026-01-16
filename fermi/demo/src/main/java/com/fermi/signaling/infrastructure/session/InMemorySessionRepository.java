package com.fermi.signaling.infrastructure.session;

import com.fermi.signaling.domain.session.Session;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemorySessionRepository implements SessionRepository {

    private final ConcurrentHashMap<String, Session> store = new ConcurrentHashMap<>();

    @Override
    public Session save(Session session) {
        store.put(session.getSessionId(), session);
        return session;
    }

    @Override
    public Optional<Session> findById(String sessionId) {
        return Optional.ofNullable(store.get(sessionId));
    }
}