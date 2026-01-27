package com.fermi.signaling.view;

import com.fermi.signaling.application.agent.AgentAuthService;
import com.fermi.signaling.api.agent.dto.LoginRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AgentController {

    private final AgentAuthService agentAuthService;

    public AgentController(AgentAuthService agentAuthService) {
        this.agentAuthService = agentAuthService;
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

    @GetMapping("/join")
    public String join() {
        return "join";
    }

    @GetMapping("/login")
    public String loginPage(Model model, HttpSession session) {
        if (session.getAttribute("agentId") != null) {
            return "redirect:/agent";
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(LoginRequest loginRequest, RedirectAttributes redirectAttributes, HttpSession session) {
        boolean loggedIn = agentAuthService.login(loginRequest.getUsername(), loginRequest.getPassword());
        if (loggedIn) {
            session.setAttribute("agentId", loginRequest.getUsername());
            return "redirect:/agent";
        }
        else {
            redirectAttributes.addFlashAttribute("error", "Invalid username or password");
            return "redirect:/login";
        }
    }

    @GetMapping("/agent")
    public String agent(HttpSession session, Model model) {
        String agentId = (String) session.getAttribute("agentId");
        if (agentId == null) {
            return "redirect:/login";
        }
        model.addAttribute("agentId", agentId);
        return "agent";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/end")
    public String endPage() {
        return "end";
    }
}