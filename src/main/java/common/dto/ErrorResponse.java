package common.dto;

public record ErrorResponse(
        String code,
        String message
        ) { }