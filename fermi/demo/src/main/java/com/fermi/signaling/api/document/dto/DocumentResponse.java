package com.fermi.signaling.api.document.dto;

import java.util.List;

public record DocumentResponse(String pdfUrl, List<Object> fields) {
}
