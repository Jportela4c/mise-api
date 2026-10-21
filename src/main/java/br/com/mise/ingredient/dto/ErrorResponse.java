package br.com.mise.ingredient.dto;

public record ErrorResponse(
        String code,
        String message
        ) { }