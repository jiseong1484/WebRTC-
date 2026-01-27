package com.fermi.signaling.application.agent;

import com.fermi.signaling.domain.agent.Agent;
import com.fermi.signaling.domain.agent.AgentRepository;
import com.fermi.signaling.domain.agent.AgentStatus;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AgentAuthService {

    private final AgentRepository agentRepository;

    public AgentAuthService(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    @PostConstruct
    public void initialize() {
        if (agentRepository.findByUsername("agent").isEmpty()) {
            // Using "agent" as both agentId and username for simplicity, you might want a separate agentId generator later
            agentRepository.save(new Agent("agent", "agent", "password", AgentStatus.OFFLINE));
        }
    }

    public boolean login(String username, String password) {
        return agentRepository.findByUsername(username)
                .map(agent -> agent.getPassword().equals(password))
                .orElse(false);
    }
}