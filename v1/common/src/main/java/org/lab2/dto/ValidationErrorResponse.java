package org.lab2.dto;

import java.util.List;

public record ValidationErrorResponse(String message, List<ErrorDescription> errors) {
}
