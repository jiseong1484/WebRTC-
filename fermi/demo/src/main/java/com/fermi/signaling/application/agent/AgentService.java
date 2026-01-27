package com.fermi.signaling.application.agent;

import com.fermi.signaling.domain.agent.*;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AgentService {
    private final AgentRepository agentRepository;

    public AgentService(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    public void setAvailable(String agentId) {
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new IllegalArgumentException("Agent not found with ID: " + agentId));
        agent.setStatus(AgentStatus.AVAILABLE);
        agentRepository.save(agent);
    }

    public void setBusy(String agentId) {
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new IllegalArgumentException("Agent not found with ID: " + agentId));
        agent.setStatus(AgentStatus.BUSY);
        agentRepository.save(agent);
    }

    public Optional<Agent> findById(String agentId) {
        return agentRepository.findById(agentId);
    }

    public Optional<Agent> findFirstAvailableAgent() {
        return agentRepository.findFirstByStatus(AgentStatus.AVAILABLE);
    }
}