package com.fermi.signaling.api.feedback;

import com.fermi.signaling.api.feedback.dto.CreateFeedbackRequest;
import com.fermi.signaling.application.feedback.FeedbackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    public ResponseEntity<Void> submitFeedback(@RequestBody CreateFeedbackRequest request) {
        feedbackService.saveFeedback(request);
        return ResponseEntity.ok().build();
    }
}
