package com.fermi.signaling.api.feedback.dto;

public record CreateFeedbackRequest(
    String sessionId,
    int rating,
    String comment
) {}
