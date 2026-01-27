package com.fermi.signaling.application.feedback;

import com.fermi.signaling.api.feedback.dto.CreateFeedbackRequest;
import com.fermi.signaling.application.session.SessionService;
import com.fermi.signaling.domain.feedback.Feedback;
import com.fermi.signaling.domain.feedback.FeedbackRepository;
import com.fermi.signaling.domain.session.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final SessionService sessionService;

    public FeedbackService(FeedbackRepository feedbackRepository, SessionService sessionService) {
        this.feedbackRepository = feedbackRepository;
        this.sessionService = sessionService;
    }

    @Transactional
    public void saveFeedback(CreateFeedbackRequest request) {
        if (request == null || request.sessionId() == null || request.sessionId().isBlank()) {
            // Or throw an exception, depending on desired error handling
            return;
        }

        Session session = sessionService.getSessionOrThrow(request.sessionId());
        
        Feedback feedback = new Feedback(
            request.sessionId(),
            session.getAgentId(),
            request.rating(),
            request.comment()
        );
        feedbackRepository.save(feedback);
    }
}
