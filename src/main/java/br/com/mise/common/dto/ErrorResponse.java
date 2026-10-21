package br.com.mise.common.dto;

public record ErrorResponse(
        String code,
        String message
        ) { }