package com.fermi.signaling.api.document.dto;

import java.util.Map;

public record FinalizeDocumentRequest(
    String documentName,
    Map<String, Object> fieldValues
) {}
