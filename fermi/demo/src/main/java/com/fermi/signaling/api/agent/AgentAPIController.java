package com.fermi.signaling.api.agent;

import com.fermi.signaling.application.agent.AgentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/agents")
public class AgentAPIController {

    private final AgentService agentService;

    public AgentAPIController(AgentService agentService) {
        this.agentService = agentService;
    }

    @PostMapping("/{agentId}/available")
    public ResponseEntity<Void> setAgentAvailable(@PathVariable String agentId) {
        agentService.setAvailable(agentId);
        return ResponseEntity.ok().build();
    }
}